package fr.simonviel.skywars.tasks;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.utils.FileManager;

public class GameManager extends BukkitRunnable{
	
	private FileConfiguration skyConfigYML;
	private FileManager fileManager;
	private int timer;
	private main main;

	public GameManager(main main) {
		fileManager = main.getFileManager();
		skyConfigYML = fileManager.getSkyConfigYML();
		this.main = main;
		this.timer = skyConfigYML.getInt("params.game.refill-chest-timer");
	}
	
	@Override
	public void run() {
		
		
		
		if(timer == 0) {
			
			main.getChestRefill().refillAllChests();

			for(Player players : main.getPlayers()) {
				
				fileManager.sendTitle(players, "titles.game.chests-refilled", -1);
				
			}
			
			timer = skyConfigYML.getInt("params.game.refill-chest-timer");
		}
		
		
		timer--;
	}
	
	

}
