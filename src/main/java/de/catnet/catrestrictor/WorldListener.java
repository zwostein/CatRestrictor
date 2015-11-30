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

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;


/**
 * Loads world configurations as needed.
 * @author zwostein
 */
public class WorldListener implements Listener
{
	@EventHandler( priority = EventPriority.LOWEST )
	public void onWorldLoad( WorldLoadEvent event )
	{
		ConfigurationSection worldsSection = CatRestrictor.getInstance().getConfig().getConfigurationSection( "worlds" );
		if( worldsSection == null )
		{
			CatRestrictor.getInstance().getLogger().log( Level.WARNING, "No Worlds are configured!" );
			return;
		}

		ConfigurationSection worldSection = worldsSection.getConfigurationSection( event.getWorld().getName() );
		if( worldSection == null )
		{
			CatRestrictor.getInstance().getLogger().log( Level.WARNING, "Empty configuration for world \"{0}\"!", event.getWorld().getName() );
			return;
		}

		WorldConfig worldConfig = WorldConfigSerializer.newWorldConfigFromConfigurationSection( worldSection );

		CatRestrictor.setWorldConfig( event.getWorld().getName(), worldConfig );
/*
		CatRestrictor.getInstance().getLogger().log( Level.INFO, "Loaded config for \"{0}\": {1}",
			new Object[] { event.getWorld().getName(), worldConfig.toString() } );
*/
		CatRestrictor.getInstance().getLogger().log( Level.INFO, "Loaded config for \"{0}\"", event.getWorld().getName() );
	}
}
