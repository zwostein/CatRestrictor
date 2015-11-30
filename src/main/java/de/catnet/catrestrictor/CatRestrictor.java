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


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * The plugin's main class.
 * @author zwostein
 */
public final class CatRestrictor extends JavaPlugin
{
	private static CatRestrictor instance = null;

	private static final Map<String, WorldConfig> worldConfigs = new HashMap<String, WorldConfig>();

	/**
	 * Returns a world's configuration.
	 * @param world The world's name.
	 * @return A valid world configuration or null if not existing.
	 */
	public static WorldConfig getWorldConfig( String world )
	{
		return worldConfigs.get( world );
	}

	/**
	 * Returns a world's configuration.
	 * @param world A world object.
	 * @return A valid world configuration or null if not existing.
	 */
	public static WorldConfig getWorldConfig( World world )
	{
		return getWorldConfig( world.getName() );
	}

	/**
	 * Returns all available world configurations
	 * @return A map using the world's name as key and its configuration as value.
	 */
	public static Map<String, WorldConfig> getWorldConfigs()
	{
		return worldConfigs;
	}

	/**
	 * Add a world's configuration to the database.
	 * @param name The world's name.
	 * @param config A world configuration.
	 */
	public static void setWorldConfig( String name, WorldConfig config )
	{
		worldConfigs.put( name, config );
	}


	private InteractionListener playerInteractionListener = null;
	private WorldListener worldListener = null;

	/**
	 * Returns the plugin's instance.
	 * @return The plugin's instance or null if not enabled.
	 */
	public static CatRestrictor getInstance()
	{
		return instance;
	}

	@Override
	public void onEnable()
	{
		if( instance != null )
		{
			this.getLogger().log( Level.WARNING, "Another instance of this plugin has already been enabled - ignoring!" );
			return;
		}
		instance = this;

		this.saveDefaultConfig();
/*
		ConfigurationSection worldsSection = this.getConfig().getConfigurationSection( "worlds" );
		if( worldsSection == null )
		{
			this.getLogger().log( Level.WARNING, "No Worlds are configured!" );
		} else
		{
			for( String worldName : worldsSection.getKeys( false ) )
			{
				ConfigurationSection worldSection = worldsSection.getConfigurationSection( worldName );
				if( worldSection == null )
				{
					this.getLogger().log( Level.WARNING, "Could not read configuration for \"{0}\"!", worldName );
					continue;
				}

				WorldConfig worldConfig = WorldConfigSerializer.newWorldConfigFromConfigurationSection( worldSection );

				this.worldConfigs.put( worldName, worldConfig );

				this.getLogger().log( Level.INFO, "Loaded config for \"{0}\": {1}",
					new Object[] { worldName, worldConfig.toString() } );
			}
		}
*/
		this.worldListener = new WorldListener();
		this.getServer().getPluginManager().registerEvents( this.worldListener, this );

		this.playerInteractionListener = new InteractionListener();
		this.getServer().getPluginManager().registerEvents( this.playerInteractionListener, this );

		OperatorCommandExecutor commandExecutor = new OperatorCommandExecutor();

		this.getLogger().log( Level.INFO, "Enabled {0}", getDescription().getFullName() );
	}

	@Override
	public void onDisable()
	{
		if( instance == null )
		{
			this.getLogger().log( Level.WARNING, "The instance of this plugin has already been disabled - ignoring!" );
			return;
		}
		instance = null;
		this.getLogger().log( Level.INFO, "Disabled {0}", getDescription().getFullName() );
	}
}
