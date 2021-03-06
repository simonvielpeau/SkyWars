package fr.simonviel.skywars.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.simonviel.skywars.main;

public class SkyWin {
	
	private main main;
	private boolean taskAlreadyBegun;
	
	public SkyWin(main main) {
		this.main = main;
	}

	public void checkWin() {
		
		if(Bukkit.getOnlinePlayers().size() == 0 || main.getPlayers().size() == 0) {
			if(taskAlreadyBegun) return;
			main.getStateManager().setState(GameState.FINISH);
			//Bukkit.spigot().restart();
			Bukkit.shutdown();
			return;
		}
		
		
		if(main.getPlayers().size() == 1) {
			if(taskAlreadyBegun) return;
			
			taskAlreadyBegun = true;
			main.getStateManager().setState(GameState.PREFINISH);
			
			Player winner = main.getPlayers().get(0);

			for(String message : main.getFileManager().getStringList("messages.finished.winner", winner)) {
				Bukkit.broadcastMessage(message);
			}
			
			main.getFileManager().sendTitle(winner, "titles.finished.youwon", -1);
		
			int timerBeforeEndMessage = 3;
			int timer = main.getFileManager().getSkyConfigYML().getInt("params.finished.timer");
			
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				@Override
				public void run() {
					Bukkit.broadcastMessage(main.getFileManager().getLine("messages.finished.timer-message", null, timer, -1));
				}
			}, timerBeforeEndMessage*20);
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				@Override
				public void run() {
					for(Player pls : Bukkit.getOnlinePlayers()) {
						pls.kickPlayer(main.getFileManager().getLine("messages.finished.kick"));
					}
					main.getStateManager().setState(GameState.FINISH);
					//Bukkit.spigot().restart();;
					Bukkit.shutdown();
				}
			}, 20*(timer+timerBeforeEndMessage));
					
		}
		
	}

}
