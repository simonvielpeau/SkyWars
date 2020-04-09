package fr.simonviel.skywars.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.game.GameState;
import fr.simonviel.skywars.game.GameStateManager;
import fr.simonviel.skywars.tasks.AutoStart;
import fr.simonviel.skywars.utils.FileManager;

public class SkyJoin implements Listener{
	
	private main main;
	private FileManager fileManager;
	private GameStateManager stateManager;
	
	int task;

	public SkyJoin(main main) {
		this.main = main;
		this.fileManager = main.getFileManager();
		this.stateManager = main.getStateManager();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		e.setJoinMessage(null);
		
		player.teleport(fileManager.getConfigLoc("hub"));
		player.setFoodLevel(20);
		player.setHealth(player.getMaxHealth());
		player.setExp((float) 0.0);
		for (PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
		if(!canHeJoin(player)) return;
		
		if(!main.getPlayers().contains(player)) {
			main.getPlayers().add(player);
		}

		Bukkit.broadcastMessage(fileManager.getLine("messages.wait.join", player, -1, main.getPlayers().size()));
		
		fileManager.sendTitle(player, "titles.wait.join", -1);
		
		player.setGameMode(GameMode.ADVENTURE);
		
		if(main.getPlayers().size() == fileManager.getSkyConfigYML().getInt("params.wait.size-to-begin")) {
			AutoStart start = new AutoStart(main);
			start.runTaskTimer(main, 0, 20);
			
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		if(main.getPlayers().contains(player)) {
			main.getPlayers().remove(player);
			e.setQuitMessage(null);
			Bukkit.broadcastMessage(fileManager.getLine("messages.wait.quit", player, -1, main.getPlayers().size()));
		}
		
		if(!main.getStateManager().isState(GameState.LOBBY))main.checkWin();
		
		
	}
	
	public boolean canHeJoin(Player player) {
		if(!stateManager.canJoin()) {
			player.sendMessage(fileManager.getLine("messages.wait.already-begun"));
			player.setGameMode(GameMode.SPECTATOR);
			return false;
		}
		if(main.getPlayers().size() +1 > fileManager.getSkyConfigYML().getInt("params.wait.max-size")) {
			player.sendMessage(fileManager.getLine("messages.wait.toomuch-players"));
			player.setGameMode(GameMode.SPECTATOR);
			return false;
		}
	
		if(!main.getCageLocations().enoughCages(main.getPlayers())) {
			player.sendMessage(fileManager.getLine("messages.wait.toomuch-players"));
			player.setGameMode(GameMode.SPECTATOR);
			return false;
		}
		return true;
	}
	
}
