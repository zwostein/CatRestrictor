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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.hanging.*;


/**
 * Listens for player interactions and cancels them if needed.
 * @author zwostein
 */
public class InteractionListener implements Listener
{
	private WorldConfig getWorldConfig( World world )
	{
		WorldConfig wc = CatRestrictor.getInstance().getWorldConfig( world );
		if( wc == null )
			wc = new WorldConfig();
		return wc;
	}

	private void teleportIfNeeded( Player player, WorldConfig worldConfig )
	{
		Location destination = worldConfig.getTeleportDestination();
		if( !worldConfig.isTeleportEnabled() || destination == null )
			return;
		CatRestrictor.getInstance().getLogger().log( Level.INFO, "Teleporting \"{0}\" to {1}",
			new Object[] { player.getName(),destination } );
		player.teleport( destination, PlayerTeleportEvent.TeleportCause.PLUGIN );
	}

	private void interventIfNeeded( Cancellable event, Player player, WorldConfig worldConfig )
	{
		if( !worldConfig.isEventRestrictedForPlayer( event.getClass(), player ) )
			return;
		CatRestrictor.getInstance().getLogger().log( Level.INFO, "Cancelling event \"{0}\" from \"{1}\"",
			new Object [] { event.getClass(), player.getName() } );
		if( worldConfig.getInterventionMessage() != null )
			player.sendMessage( worldConfig.getInterventionMessage() );
		event.setCancelled( true );
		this.teleportIfNeeded( player, worldConfig );
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onPlayerInteraction( PlayerInteractEvent event )
	{
		if( event.getClickedBlock() == null )
			return;

		Player player = event.getPlayer();
		WorldConfig worldConfig = this.getWorldConfig( player.getWorld() );
		this.interventIfNeeded( event, player, worldConfig );
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onPlayerInteraction( PlayerInteractEntityEvent event )
	{
		Player player = event.getPlayer();
		WorldConfig worldConfig = this.getWorldConfig( player.getWorld() );
		this.interventIfNeeded( event, player, worldConfig );
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onEntityDamageByEntityEvent( EntityDamageByEntityEvent event )
	{
		Entity damager = event.getDamager();
		if( !( damager instanceof Player) )
			return;
		Player player = (Player)damager;
		WorldConfig worldConfig = this.getWorldConfig( player.getWorld() );
		this.interventIfNeeded( event, player, worldConfig );
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onBlockPlaceEvent( BlockPlaceEvent event )
	{
		Player player = event.getPlayer();
		WorldConfig worldConfig = this.getWorldConfig( player.getWorld() );
		this.interventIfNeeded( event, player, worldConfig );
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onBlockBreakEvent( BlockBreakEvent event )
	{
		Player player = event.getPlayer();
		WorldConfig worldConfig = this.getWorldConfig( player.getWorld() );
		this.interventIfNeeded( event, player, worldConfig );
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onVehicleDamageEvent( VehicleDamageEvent event )
	{
		Entity atacker = event.getAttacker();
		if( !( atacker instanceof Player) )
			return;
		Player player = (Player)atacker;
		WorldConfig worldConfig = this.getWorldConfig( player.getWorld() );
		this.interventIfNeeded( event, player, worldConfig );
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onHangingBreakByEntityEvent( HangingBreakByEntityEvent event )
	{
		Entity remover = event.getRemover();
		if( !( remover instanceof Player) )
			return;
		Player player = (Player)remover;
		WorldConfig worldConfig = this.getWorldConfig( player.getWorld() );
		this.interventIfNeeded( event, player, worldConfig );
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onPlayerArmorStandManipulateEvent( PlayerArmorStandManipulateEvent event )
	{
		Player player = event.getPlayer();
		WorldConfig worldConfig = this.getWorldConfig( player.getWorld() );
		this.interventIfNeeded( event, player, worldConfig );
	}
}
