package fr.simonviel.skywars.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.simonviel.skywars.main;

public class SkyWin {
	
	private main main;
	
	public SkyWin(main main) {
		this.main = main;
	}

	public void checkWin() {
		
		if(Bukkit.getOnlinePlayers().size() == 0 ||main.getPlayers().size() == 0) {
			Bukkit.shutdown();
			return;
		}
		
		
		if(main.getPlayers().size() == 1) {
			main.getStateManager().setState(GameState.FINISH);
			
			Player winner = main.getPlayers().get(0);
			
			for(String message : main.getFileManager().getStringList("messages.finished.winner", winner)) {
				
				Bukkit.broadcastMessage(message);
				
			}
			
			main.getFileManager().sendTitle(winner, "titles.finished.youwon", -1);
		
			int timer = main.getFileManager().getSkyConfigYML().getInt("params.finished.timer");
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				@Override
				public void run() {
					Bukkit.broadcastMessage(main.getFileManager().getLine("messages.finished.timer-message", null, timer, -1));
				}
			}, 3*20);
			
			Bukkit.getScheduler().runTaskLater(main, new Runnable() {
				@Override
				public void run() {
					for(Player pls : Bukkit.getOnlinePlayers()) {
						pls.kickPlayer(main.getFileManager().getLine("messages.finished.kick"));
					}
					Bukkit.shutdown();
				}
			}, 20*timer);
					
		}
		
	}

}
