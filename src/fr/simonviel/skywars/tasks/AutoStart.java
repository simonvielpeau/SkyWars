package fr.simonviel.skywars.tasks;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.game.GameState;
import fr.simonviel.skywars.game.GameStateManager;
import fr.simonviel.skywars.utils.FileManager;

public class AutoStart extends BukkitRunnable{
	
	private FileManager fileManager;
	private main main;
	private GameStateManager stateManager;
	int timer;

	public AutoStart(main main) {
		this.main = main;
		this.fileManager = main.getFileManager();
		this.timer = fileManager.getSkyConfigYML().getInt("params.wait.initial-timer");
		this.stateManager = main.getStateManager();
	}

	@Override
	public void run() {
			
		for(Player players : main.getPlayers()) {
			players.setLevel(timer);
		}
		
		if(timer == 30 ||timer == 15 ||timer == 10||timer==5||timer<=3 && timer >0) {
			for(Player players : main.getPlayers()) {
				fileManager.sendTitle(players, "titles.wait.timer", timer);
			}
		}
		
		if(timer == 30 || timer==15 ||timer==10||timer==5) {
			for(Player pls : main.getPlayers()) {
				if(main.getKitManager().getPlayerKit(pls) == null) {
					pls.sendMessage(fileManager.getLine("messages.wait.kits.choose-one"));
				}
			}
		}
		
		if(main.getPlayers().size() < main.getFileManager().getSkyConfigYML().getInt("params.wait.size-to-begin")) {
			for(Player players : main.getPlayers()) {
				players.sendMessage(fileManager.getLine("messages.wait.stop-timer", null, -1, main.getPlayers().size()));
				players.setLevel(0);
			}
			cancel();
		}
		
		if(timer == 0) {
			for(Player players : main.getPlayers()) {
				fileManager.sendTitle(players, "titles.wait.go", -1);
				players.setLevel(0);
				players.setGameMode(GameMode.SURVIVAL);
			}
			cancel();
			stateManager.setState(GameState.PREGAME);
			main.getCageLocations().TeleportPlayers(main.getPlayers());
			
			PreGameManager gameManager = new PreGameManager(main);
			gameManager.runTaskTimer(main, 0, 20);
		}
		
		
		
		timer--;
	}
	
}
