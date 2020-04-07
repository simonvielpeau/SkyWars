package fr.simonviel.skywars.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.world.PortalCreateEvent;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.game.GameState;
import fr.simonviel.skywars.game.GameStateManager;

public class GameListeners implements Listener {

	private main main;
	private GameStateManager stateManager;
	
	public GameListeners(main main) {
		this.main = main;
		this.stateManager = this.main.getStateManager();
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(!stateManager.isState(GameState.GAME) && !e.getPlayer().hasPermission("admin.buildingame")) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(!stateManager.isState(GameState.GAME) && !e.getPlayer().hasPermission("admin.buildingame")) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!stateManager.isState(GameState.GAME)) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onNether(PortalCreateEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler 
	public void onSpawnMob(CreatureSpawnEvent e){
		e.setCancelled(true);
	}
	
	
	
}
