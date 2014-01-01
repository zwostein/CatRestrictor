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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * The plugin's main class.
 * @author zwostein
 */
public final class CatRestrictor extends JavaPlugin
{
	private static CatRestrictor instance = null;

	private final Map<String, WorldConfig> worldConfigs = new HashMap<String, WorldConfig>();
	private InteractionListener playerInteractionListener = null;

	/**
	 * Returns the plugin's instance.
	 * @return The plugin's instance or null if not enabled.
	 */
	public static CatRestrictor getInstance()
	{
		return instance;
	}

	/**
	 *
	 */
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
		for( World world : this.getServer().getWorlds() )
		{
			WorldConfig worldConfig;

			ConfigurationSection worldSection = getConfig().getConfigurationSection( "worlds." + world.getName() );
			if( worldSection == null )
			{
				this.getLogger().log( Level.WARNING, "World \"{0}\" not configured!", world.getName() );
				worldConfig = new WorldConfig();
			} else
			{
				worldConfig = WorldConfigSerializer.newWorldConfigFromConfigurationSection( worldSection );
			}

			this.getLogger().log( Level.INFO, "Loaded config for \"{0}\": {1}",
				new Object[] { world.getName(), worldConfig.toString() } );
			this.worldConfigs.put( world.getName(), worldConfig );
		}

		this.playerInteractionListener = new InteractionListener();
		this.getServer().getPluginManager().registerEvents( this.playerInteractionListener, this );

		OperatorCommandExecutor commandExecutor = new OperatorCommandExecutor();
		getCommand( "cr_add" ).setExecutor( commandExecutor );
		getCommand( "cr_rm" ).setExecutor( commandExecutor );
		getCommand( "cr_reload" ).setExecutor( commandExecutor );

		this.getLogger().log( Level.INFO, "Enabled {0}", getDescription().getFullName() );
	}

	/**
	 *
	 */
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

	/**
	 * Returns a world's configuration.
	 * @param world The world's name.
	 * @return A valid world configuration or null if not existing.
	 */
	public WorldConfig getWorldConfig( String world )
	{
		return this.worldConfigs.get( world );
	}

	/**
	 * Returns a world's configuration.
	 * @param world A world object.
	 * @return A valid world configuration or null if not existing.
	 */
	public WorldConfig getWorldConfig( World world )
	{
		return this.getWorldConfig( world.getName() );
	}

	/**
	 * Returns all available world configurations
	 * @return A map using the world's name as key and its configuration as value.
	 */
	public Map<String, WorldConfig> getWorldConfigs()
	{
		return this.worldConfigs;
	}
}
