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
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.World;


/**
 * World configuration.
 * @author zwostein
 */
public class WorldConfig
{


	private Whitelist whitelist = null;
	private boolean teleportEnabled = false;
	private Location cachedTeleportDestination = null;
	private String teleportDestination_worldName;
	private double teleportDestination_x;
	private double teleportDestination_y;
	private double teleportDestination_z;
	private float teleportDestination_pitch;
	private float teleportDestination_yaw;
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
		if( this.cachedTeleportDestination != null )
			return this.cachedTeleportDestination;

		World world = CatRestrictor.getInstance().getServer().getWorld( this.teleportDestination_worldName );
		if( world == null )
			CatRestrictor.getInstance().getLogger().log( Level.WARNING, "The world \"{0}\" could not be found, using default instead", this.teleportDestination_worldName );

		this.cachedTeleportDestination = new Location(
			world,
			this.teleportDestination_x,
			this.teleportDestination_y,
			this.teleportDestination_z,
			this.teleportDestination_pitch,
			this.teleportDestination_yaw
		);
		return this.cachedTeleportDestination;
	}

	public void setTeleportEnabled( boolean enable )
	{
		this.teleportEnabled = enable;
	}

	public void setTeleportDestination( String worldName, double x, double y, double z, float pitch, float yaw )
	{
		this.teleportDestination_worldName = worldName;
		this.teleportDestination_x = x;
		this.teleportDestination_y = y;
		this.teleportDestination_z = z;
		this.teleportDestination_pitch = pitch;
		this.teleportDestination_yaw = yaw;
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

		if( this.interventionMessage != null )
			ret += ",\n\tinterventionMessage=\"" + this.interventionMessage + "\"";

		ret += "\n}";
		return ret;
	}
}
