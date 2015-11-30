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


import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;


/**
 * World configuration.
 * @author zwostein
 */
public class WorldConfig
{


	private Whitelist whitelist = null;
	private boolean teleportEnabled = false;
	private Location teleportDestination = null;
	private String interventionMessage = null;
	private final Set<Class> restrictedEvents = new HashSet<Class>();

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

	public boolean addEventRestriction( Class event )
	{
		return restrictedEvents.add( event );
	}

	public boolean removeEventRestriction( Class event )
	{
		return restrictedEvents.remove( event );
	}

	public boolean isEventRestricted( Class event )
	{
		return restrictedEvents.contains( event );
	}

	public boolean isEventRestrictedForPlayer( Class event, Player player )
	{
		if( !isEventRestricted( event ) )
			return false;
		if( this.whitelist == null )
			return true;
		return !whitelist.isListed( player );
	}

	public boolean isTeleportEnabled()
	{
		return this.teleportEnabled;
	}

	public Location getTeleportDestination()
	{
		return this.teleportDestination;
	}

	public void setTeleportEnabled( boolean enable )
	{
		this.teleportEnabled = enable;
	}

	public void setTeleportDestination( Location destination )
	{
		this.teleportDestination = destination;
	}

	public String getInterventionMessage()
	{
		return interventionMessage;
	}

	public void setInterventionMessage( String interventionMessage )
	{
		this.interventionMessage = interventionMessage;
	}

	@Override
	public String toString()
	{
		String ret = "WorldConfig\n{";

		ret += "\n\trestrictedEvents=\"";
		for( Class c: this.restrictedEvents )
		{
			ret += c.getSimpleName() + ",";
		}
		if( ret.length() > 0 )
			ret = ret.substring( 0, ret.length()-1 );
		ret += "\"";

		if( this.whitelist != null )
			ret += ",\n\twhitelist=\"" + this.whitelist.getFile().getPath() + "\"";

		ret += ",\n\tteleportEnabled=" + this.teleportEnabled;
		if( this.teleportDestination != null )
			ret += ",\n\tteleportDestination=" + this.teleportDestination.toString();


		if( this.interventionMessage != null )
			ret += ",\n\tinterventionMessage=\"" + this.interventionMessage + "\"";

		ret += "\n}";
		return ret;
	}
}
