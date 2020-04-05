package fr.simonviel.skywars.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CageLocations {
	
	public List<Location> cages = new ArrayList<>();
	private FileManager fileManager;
	
	
	public CageLocations(FileManager fileManager) {
		cages = fileManager.getCageList();
		this.fileManager = fileManager;
	}

	public void TeleportPlayers(ArrayList<Player> players) {
		
		for(int i = 0; i < players.size(); i++) {
			players.get(i).teleport(cages.get(i));
		}
		
	}
	
	public boolean enoughCages(ArrayList<Player> players) {
		return players.size()+1 <= cages.size();
	}
	
	public boolean enoughMinimumCages() {
		if(cages.size() < fileManager.getSkyConfigYML().getInt("params.wait.size-to-begin")) {
			return false;
		}
		return true;
		
	}
	public boolean enoughMaximumCages() {
		if(cages.size() < fileManager.getSkyConfigYML().getInt("params.wait.max-size")) {
			return false;
		}
		return true;
	}
	
	public List<Location> getCages(){
		return cages;
	}
	

}
