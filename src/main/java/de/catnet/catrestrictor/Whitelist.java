/*
CatRestrictor - A simple Bukkit plugin to secure your server.
Copyright (C) 2014  Tobias Himmer <provisorisch@online.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package de.catnet.catrestrictor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.entity.Player;


/**
 * A list of player login names.
 * @author zwostein
 */
public class Whitelist
{
	private final File file;
	private final List<UUID> uuids = new ArrayList<UUID>();

	/**
	 * Returns a file object that stores all listed names.
	 * @return The file that stores all listed names.
	 */
	public File getFile()
	{
		return this.file;
	}

	/**
	 * Creates a new whitelist object and loads it's contents - a non-existing file will be created
	 * @param whiteListFile Which file to use for storing/loading player login names.
	 */
	public Whitelist( File whiteListFile )
	{
		this.file = whiteListFile;
		File parentDirectory = this.file.getParentFile();
		if( parentDirectory != null )
		{
			if( parentDirectory.mkdirs() )
			{
				CatRestrictor.getInstance().getLogger().log( Level.INFO, "Created directory \"{0}\"", parentDirectory.getPath() );
			}
		}
		try
		{
			if( this.file.createNewFile() )
			{
				CatRestrictor.getInstance().getLogger().log( Level.INFO, "Created whitelist \"{0}\"", this.file.getPath() );
			}
		} catch( IOException ex )
		{
			CatRestrictor.getInstance().getLogger().log( Level.SEVERE, "Could not create file \"{0}\"", this.file.getPath() );
		}
		this.reload();
	}

	/**
	 * Checks if given login name is listed in this whitelist.
	 * @param uuid A player's unique id.
	 * @return True if given login name is listed in this whitelist.
	 */
	public boolean isListed( UUID uuid )
	{
		return this.uuids.contains( uuid );
	}

	/**
	 * Convenience method for {@link #isListed( UUID uuid ) isListed}
	 * @param player See {@link #isListed( UUID uuid ) isListed}
	 * @return See {@link #isListed( UUID uuid ) isListed}
	 */
	public boolean isListed( Player player )
	{
		return this.isListed( player.getUniqueId() );
	}

	/**
	 * Adds a unique id to this whitelist and also updates the corresponding file.
	 * @param uuid A unique id to add to this whitelist.
	 * @return True if given unique id has been added to the whitelist. False if unique id is already listed or an error occured while updating the whitelist's file.
	 */
	public boolean add( UUID uuid )
	{
		if( this.uuids.contains( uuid ) )
		{
			return false;
		}
		if( !this.uuids.add( uuid ) )
		{
			return false;
		}
		return this.write();
	}

	/**
	 * Convenience method for {@link #add( UUID uuid ) add}
	 * @param player See {@link #add( UUID uuid ) add}
	 * @return See {@link #add( UUID uuid ) add}
	 */
	public boolean add( Player player )
	{
		return this.add( player.getUniqueId() );
	}

	/**
	 * Removes a unique id from this whitelist and also updates the corresponding file.
	 * @param uuid A player's unique id.
	 * @return True if given player's unique id has been removed from the whitelist. False if the unique id was not listed or an error occured while updating the whitelist's file.
	 */
	public boolean remove( UUID uuid )
	{
		if( !this.uuids.contains( uuid ) )
		{
			return false;
		}
		if( !this.uuids.remove( uuid ) )
		{
			return false;
		}
		return this.write();
	}

	/**
	 * Convenience method for {@link #remove( UUID uuid ) remove}
	 * @param player See {@link #remove( UUID uuid ) remove}
	 * @return See {@link #remove( UUID uuid ) remove}
	 */
	public boolean remove( Player player )
	{
		return remove( player.getUniqueId() );
	}

	private boolean write()
	{
		try
		{
			BufferedWriter writer = new BufferedWriter( new FileWriter( this.file ) );
			for( UUID uuid : this.uuids )
			{
				writer.write( uuid.toString() );
				writer.newLine();
			}
			writer.close();
			return true;
		} catch( IOException e )
		{
			CatRestrictor.getInstance().getLogger().log( Level.SEVERE, "Could not write \"{0}\"", this.file.getPath() );
			return false;
		}
	}

	/**
	 * Reloads the current whitelist from file. Only needed if the file was modified externally.
	 * @return False on error;
	 */
	public final boolean reload()
	{
		this.uuids.clear();
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader( this.file ) );
			String line;
			while( (line = reader.readLine()) != null )
			{
				line = line.trim();
				if( !line.isEmpty() )
				{
					this.uuids.add( UUID.fromString( line ) );
				}
			}
			reader.close();
			return true;
		} catch( IOException e )
		{
			CatRestrictor.getInstance().getLogger().log( Level.SEVERE, "Could not read \"{0}\"", this.file.getPath());
			return false;
		}
	}
}
