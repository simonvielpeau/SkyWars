package fr.simonviel.skywars.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.simonviel.skywars.main;

public class KitManager {
	
	FileConfiguration skyKitsYML;
	
	private List<Kit> kits = new ArrayList<>();

	
	public KitManager(main main) {
		skyKitsYML = main.getFileManager().getSkyKitsYML();
	}
	
	public void loadKits(){
		ConfigurationSection section = skyKitsYML.getConfigurationSection("kits");
		int i = 0;
		for(String string : section.getKeys(false)) {
			ConfigurationSection kitSection = section.getConfigurationSection(string);
			
			Kit kit = new Kit(kitSection);
			kit.setSlot(i);
			i++;
			
			kits.add(kit);
		}
	}
	
	public List<Kit> getKits(){
		return kits;
	}
	
	public Kit getPlayerKit(Player p) {
		for(Kit kit : kits) {
			if(kit.containsPlayer(p)) {
				return kit;
			}
		}
		return null;
	}
	
	public void giveKit(Player p) {
		Kit plKit = getPlayerKit(p);
		p.getInventory().clear();
		if(plKit != null) {
			for(ItemStack item : plKit.getItems()) {
				p.getInventory().addItem(item);
			}
		}
		p.updateInventory();
	}


}
