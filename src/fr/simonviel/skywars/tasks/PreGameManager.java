package fr.simonviel.skywars.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.game.GameState;
import fr.simonviel.skywars.utils.FileManager;

public class PreGameManager extends BukkitRunnable {

	private int timer;
	private main main;
	private FileManager fileManager;

	public PreGameManager(main main) {
		this.main = main;
		this.fileManager = main.getFileManager();
		this.timer = fileManager.getSkyConfigYML().getInt("params.game.chrono-cage");
	}

	
	@Override
	public void run() {
		if(timer == fileManager.getSkyConfigYML().getInt("params.game.chrono-cage")) {
			for(Player player : main.getPlayers()) {
				player.sendMessage(fileManager.getLine("messages.game.timer-disparition-cage", null, timer, -1));
			}
		}
	
		for(Player player : main.getPlayers()) {
			player.setLevel(timer);
		}
		
		if(timer == 0) {
			
			for(Player player : main.getPlayers()) {
				fileManager.sendTitle(player, "titles.game.disparition-cage", -1);
				player.setLevel(0);
			}
			main.getStateManager().setState(GameState.GAME);
			main.getCageManager().destroyAllCages();
			
			GameManager gameManager = new GameManager(main);
			gameManager.runTaskTimer(main, 0, 20);
			
			cancel();
		}
		timer--;
	}


	
}
