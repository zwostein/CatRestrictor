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
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;


/**
 *
 * @author zwostein
 */
public class LocationSerializer
{
	static Location newLocationFromConfigurationSection( ConfigurationSection locationSection, Location defaultLocation )
	{
		World world = null;
		String worldName = locationSection.getString( "world" );
		if( worldName != null )
			world = CatRestrictor.getInstance().getServer().getWorld( worldName );
		if( world == null )
			world = defaultLocation.getWorld();
		return new Location(
			world,
			locationSection.getDouble( "x", defaultLocation.getX() ),
			locationSection.getDouble( "y", defaultLocation.getY() ),
			locationSection.getDouble( "z", defaultLocation.getZ() ),
			(float)locationSection.getDouble( "pitch", defaultLocation.getPitch()),
			(float)locationSection.getDouble( "yaw", defaultLocation.getYaw())
		);
	}
}
