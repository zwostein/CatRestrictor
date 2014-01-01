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


import org.bukkit.Location;
import org.bukkit.entity.Player;


/**
 * World configuration.
 * @author zwostein
 */
public class WorldConfig
{
	private Whitelist whitelist = null;
	private boolean restrictInteraction = false;
	private boolean restrictEntityInteraction = false;
	private boolean teleportEnabled = false;
	private Location teleportDestination = null;

	/**
	 * Sets this world's whitelist.
	 * @param whitelist A whitelist.
	 */
	public void setWhitelist( Whitelist whitelist )
	{
		this.whitelist = whitelist;
	}

	/**
	 * Returns this world's whitelist.
	 * @return The whitelist or null if not set.
	 */
	public Whitelist getWhitelist()
	{
		return this.whitelist;
	}

	public boolean isEntityInteractionRestricted()
	{
		return this.restrictEntityInteraction;
	}

	public boolean isInteractionRestricted()
	{
		return this.restrictInteraction;
	}

	public boolean isTeleportEnabled()
	{
		return this.teleportEnabled;
	}

	public Location getTeleportDestination()
	{
		return this.teleportDestination;
	}

	public void setEntityInteractionRestricted( boolean restrict )
	{
		this.restrictEntityInteraction = restrict;
	}

	public void setInteractionRestricted( boolean restrict )
	{
		this.restrictInteraction = restrict;
	}

	public void setTeleportEnabled( boolean enable )
	{
		this.teleportEnabled = enable;
	}

	public void setTeleportDestination( Location destination )
	{
		this.teleportDestination = destination;
	}

	public boolean isInteractionAllowed( Player player )
	{
		if( !this.restrictInteraction )
			return true;
		if( this.whitelist == null )
			return false;
		return this.whitelist.isListed( player );
	}

	public boolean isEntityInteractionAllowed( Player player )
	{
		if( !this.restrictEntityInteraction )
			return true;
		if( this.whitelist == null )
			return false;
		return this.whitelist.isListed( player );
	}

	@Override
	public String toString()
	{
		String ret = "WorldConfig{";
		if( this.whitelist != null )
			ret += "whitelist=\"" + this.whitelist.getFile().getPath() + "\",";
		ret += "restrictInteraction=" + this.restrictInteraction
			+ ",restrictEntityInteraction=" + this.restrictEntityInteraction
			+ ",teleportEnabled=" + this.teleportEnabled;
		if( this.teleportDestination != null )
			ret += ",teleportDestination=" + this.teleportDestination.toString();
		ret += "}";
		return ret;
	}
}
