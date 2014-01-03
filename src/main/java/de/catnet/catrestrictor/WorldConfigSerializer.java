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

import org.bukkit.Location;
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
		Location loc = new Location( CatRestrictor.getInstance().getServer().getWorld( worldSection.getName() ), 0.0, 0.0, 0.0 );
		ConfigurationSection locationSection = worldSection.getConfigurationSection( "teleportDestination" );
		if( locationSection != null )
			loc = LocationSerializer.newLocationFromConfigurationSection( locationSection, loc );
		worldConfig.setTeleportDestination( loc );
		return worldConfig;
	}
}
