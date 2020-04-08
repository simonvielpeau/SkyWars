package fr.simonviel.skywars.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import fr.simonviel.skywars.main;

public class KitManager {
	
	FileConfiguration skyKitsYML;
	
	private main main;
	private List<Kit> kits = new ArrayList<>();
	private HashMap<Player, Kit> playersKit= new HashMap<>();
	
	public KitManager(main main) {
		this.main = main;
		skyKitsYML = main.getFileManager().getSkyKitsYML();
	}
	
	public void loadKits(){
		ConfigurationSection section = skyKitsYML.getConfigurationSection("kits");
		int i = 0;
		for(String string : section.getKeys(false)) {
			ConfigurationSection kitSection = section.getConfigurationSection(string);
			
			Kit kit = new Kit(kitSection, main.getFileManager());
			kit.setSlot(i);
			i++;
			
			kits.add(kit);
		}
	}
	
	public List<Kit> getKits(){
		return kits;
	}
	
	public Kit getPlayerKit(Player player) {
		return playersKit.get(player);
	}
	
	public void addPlayer(Player player) {
		
		
		
	}

}
