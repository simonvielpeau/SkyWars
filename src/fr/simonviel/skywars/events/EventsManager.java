package fr.simonviel.skywars.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.kits.KitMenu;

public class EventsManager {
	
	private main main;

	public EventsManager(main main) {
		this.main = main;
	}
	
	public void registerEvents() {
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new SkyJoin(main), main);
		pm.registerEvents(new GameListeners(main), main);
		pm.registerEvents(new SkyDeath(main), main);
		pm.registerEvents(new KitMenu(main), main);
	}
	
	
}
