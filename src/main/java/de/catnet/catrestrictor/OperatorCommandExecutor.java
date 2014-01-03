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


import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


/**
 *
 * @author zwostein
 */
public class OperatorCommandExecutor implements CommandExecutor
{
	static final String COMMAND_ADD = "cr_add";
	static final String COMMAND_REMOVE = "cr_rm";
	static final String COMMAND_RELOAD = "cr_reload";

	@SuppressWarnings( "LeakingThisInConstructor" )
	public OperatorCommandExecutor()
	{
		CatRestrictor.getInstance().getCommand( COMMAND_ADD ).setExecutor( this );
		CatRestrictor.getInstance().getCommand( COMMAND_REMOVE ).setExecutor( this );
		CatRestrictor.getInstance().getCommand( COMMAND_RELOAD ).setExecutor( this );
	}

	public boolean onCommand( CommandSender sender, Command command, String commandLabel, String[] args )
	{
		if( !sender.isOp() )
		{
			CatRestrictor.getInstance().getLogger().log( Level.FINE, "Ignored command \"{0}\" from non-op \"{1}\"",
				new Object[] { command.getName(), sender.getName() } );
			sender.sendMessage( "You are no server operator!" );
			return false;
		}
		String commandName = command.getName();
		if( commandName.equals( COMMAND_ADD ) )
			return add( sender, args );
		if( commandName.equals( COMMAND_REMOVE ) )
			return remove( sender, args );
		if( commandName.equals( COMMAND_RELOAD ) )
			return reload( sender, args );
		return false;
	}

	private Whitelist retrieveWhitelist( CommandSender sender, String worldname )
	{
		WorldConfig wc = CatRestrictor.getInstance().getWorldConfig( worldname );
		if( wc == null )
		{
			sender.sendMessage( "\"" + worldname + "\" is not a known world!" );
			sender.sendMessage( "Known worlds:" );
			for( World w : CatRestrictor.getInstance().getServer().getWorlds() )
				sender.sendMessage( " * \"" + w.getName() + "\"" );
			return null;
		}
		Whitelist wl = wc.getWhitelist();
		if( wc.getWhitelist() == null )
		{
			sender.sendMessage( "\"" + worldname + "\" has no whitelist!" );
			return null;
		}
		return wl;
	}

	private boolean add( CommandSender sender, String[] args )
	{
		if( args.length != 2 )
			return false;
		Whitelist wl = retrieveWhitelist( sender, args[0] );
		if( wl == null )
			return true;
		if( wl.add( args[1] ) )
			sender.sendMessage( "Added \"" + args[1] + "\" to whitelist of \"" + args[0] + "\"" );
		else
			sender.sendMessage( "\"" + args[1] + "\" already in whitelist of \"" + args[0] + "\"" );
		return true;
	}

	private boolean remove( CommandSender sender, String[] args )
	{
		if( args.length != 2 )
			return false;
		Whitelist wl = retrieveWhitelist( sender, args[0] );
		if( wl == null )
			return true;
		if( wl.remove( args[1] ) )
			sender.sendMessage( "Removed \"" + args[1] + "\" from whitelist of \"" + args[0] + "\"" );
		else
			sender.sendMessage( "\"" + args[1] + "\" not in whitelist of \"" + args[0] + "\"" );
		return true;
	}

	private boolean reload( CommandSender sender, String[] args )
	{
		if( args.length != 0 )
			return false;

		for( WorldConfig wc : CatRestrictor.getInstance().getWorldConfigs().values() )
			if( wc.getWhitelist() != null )
				wc.getWhitelist().reload();

		sender.sendMessage( "Whitelists reloaded" );
		return true;
	}
}
