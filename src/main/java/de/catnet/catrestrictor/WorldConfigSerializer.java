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


import java.io.File;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author zwostein
 */
public class WorldConfigSerializer
{
	static WorldConfig newWorldConfigFromConfigurationSection( ConfigurationSection worldSection )
	{
		WorldConfig worldConfig = new WorldConfig();
		String whitelistName = worldSection.getString( "whitelist" );
		if( whitelistName != null )
			worldConfig.setWhitelist( new Whitelist( new File( CatRestrictor.getInstance().getDataFolder(), whitelistName ) ) );
		worldConfig.setInterventionMessage( worldSection.getString( "interventionMessage" ) );

		List<String> restrictedEventNames = worldSection.getStringList( "restrictedEvents" );
		for( String s : restrictedEventNames )
		{
			try
			{
				worldConfig.addEventRestriction( Class.forName( s ) );
			} catch( ClassNotFoundException ex )
			{
				CatRestrictor.getInstance().getLogger().log( Level.WARNING, "Event \"{0}\" could not be found and will be ignored", s );
			}
		}

		worldConfig.setTeleportEnabled( worldSection.getBoolean( "enableTeleport", false ) );
		ConfigurationSection locationSection = worldSection.getConfigurationSection( "teleportDestination" );
		String worldName = locationSection.getString( "world" );
		double x = locationSection.getDouble( "x", 0.0 );
		double y = locationSection.getDouble( "y", 0.0 );
		double z = locationSection.getDouble( "z", 0.0 );
		float pitch = (float)locationSection.getDouble( "pitch", 0.0 );
		float yaw = (float)locationSection.getDouble( "yaw", 0.0 );
		worldConfig.setTeleportDestination( worldName, x, y, z, pitch, yaw );
		return worldConfig;
	}
}
