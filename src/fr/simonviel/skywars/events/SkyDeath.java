package fr.simonviel.skywars.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.game.GameState;
import fr.simonviel.skywars.utils.FileManager;
import net.minecraft.server.v1_11_R1.PacketPlayInClientCommand;
import net.minecraft.server.v1_11_R1.PacketPlayInClientCommand.EnumClientCommand;

public class SkyDeath implements Listener{

	private main main;
	private FileManager fileManager;
	
	public SkyDeath(main main) {
		this.main = main;
		fileManager = main.getFileManager();
	}
	
	
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent event) {
		
		if(!main.getStateManager().isState(GameState.GAME)) return;
		if(!(event.getEntity() instanceof Player)) return;
		
		Player player = (Player) event.getEntity();
		
		if(!(event.getDamage() >= player.getHealth())) return;
		event.setCancelled(true);
		
		
		Player damager = null;
		if(event.getDamager() instanceof Player) damager = (Player) event.getDamager();
		
		if(event.getDamager() instanceof Arrow ) {
			Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof Player) {
				damager = (Player) arrow.getShooter();
			}
		}
		
		fileManager.sendTitle(player, "titles.game.death", -1);

		if(damager != null) {
			death(player,"messages.game.death.pvp",damager);
			return;
		}
		
		death(player,"messages.game.death.alone" ,damager);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!main.getStateManager().isState(GameState.GAME)) return;
		
		if(!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player) e.getEntity();
		
		if(e.getCause() == DamageCause.FALL) {
			if(e.getDamage() >= p.getHealth()) {
			
				e.setCancelled(true);
				fileManager.sendTitle(p, "titles.game.death", -1);
				Player killer = p.getKiller();
				if(killer != null) {
					death(p, "messages.game.death.chute.killed", killer);
					return;
						
				}

				death(p, "messages.game.death.chute.alone", killer);
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		
		if(!main.getStateManager().isState(GameState.GAME)) {
			if(p.getLocation().getY() <= fileManager.getSkyChestsYML().getInt("params.game.y-fall")) {
				p.teleport(fileManager.getConfigLoc("hub"));
				return;
			}
		}
		
		if(p.getLocation().getY() <= fileManager.getSkyChestsYML().getInt("params.game.y-fall")) {
			fileManager.sendTitle(p, "titles.game.death", -1);
			Player killer = p.getKiller();
			
			if(killer != null) {
				death(p, "messages.game.death.chute.killed", killer);
				return;
			}

			death(p, "messages.game.death.chute.alone", killer);
		}
	}
	
	
	
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(!main.getStateManager().isState(GameState.GAME)) return;
		final Player p = event.getEntity();
		final Player killer = p.getKiller();
		event.setDeathMessage(null);
		Bukkit.getScheduler().runTaskLater(main, new Runnable() {

			@Override
			public void run() {
				PacketPlayInClientCommand cmd = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
				((CraftPlayer) p).getHandle().playerConnection.a(cmd);
				fileManager.sendTitle(p, "titles.game.death", -1);
					
				
				if(killer != null) {
					death(p, "messages.game.death.pvp", killer);
					return;
				}

				death(p, "messages.game.death.alone", killer);
			}		
			
		}, 5L);
	}
	
	public void death(Player player, String path, Player killer) {
		
		for(Player pls : main.getPlayers()) {
			pls.sendMessage(fileManager.getLine(path, player, killer));
		}
		
		player.teleport(fileManager.getConfigLoc("hub"));
		player.setGameMode(GameMode.SPECTATOR);
		main.getPlayers().remove(player);
		main.checkWin();
	}
	
}
