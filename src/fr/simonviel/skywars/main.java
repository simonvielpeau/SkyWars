package fr.simonviel.skywars;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.simonviel.skywars.chests.ChestRefill;
import fr.simonviel.skywars.chests.ItemsRandom;
import fr.simonviel.skywars.commands.CommandSet;
import fr.simonviel.skywars.events.EventsManager;
import fr.simonviel.skywars.game.GameState;
import fr.simonviel.skywars.game.GameStateManager;
import fr.simonviel.skywars.game.SkyWin;
import fr.simonviel.skywars.kits.KitManager;
import fr.simonviel.skywars.utils.CageLocations;
import fr.simonviel.skywars.utils.CageManager;
import fr.simonviel.skywars.utils.FileManager;
import fr.simonviel.skywars.utils.Title;

public class main extends JavaPlugin{

	private FileManager fileManager;
	private GameStateManager stateManager;
	private CageLocations cageLocations;
	private CageManager cageManager;
	private Title sendTitles;
	private ChestRefill chestRefill;
	private ItemsRandom itemsRandom;
	private KitManager kitManager;
	private int beforeMaxSize = 0;
	private ArrayList<Player> players = new ArrayList<>();
	private SkyWin skyWin;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		sendTitles = new Title();
		fileManager = new FileManager(this);
		stateManager = new GameStateManager();
		stateManager.setState(GameState.LOBBY);
		cageLocations = new CageLocations(fileManager);
		cageManager = new CageManager(this);
		itemsRandom = new ItemsRandom(this);
		chestRefill = new ChestRefill(this);
		skyWin = new SkyWin(this);
		kitManager = new KitManager(this);
		kitManager.loadKits();
		checkCages();

		getCommand("set").setExecutor(new CommandSet(this));
		new EventsManager(this).registerEvents();
		cageManager.cageInitializer();
		chestRefill.refillAllChests();
		meteo();
	}
	

	@Override
	public void onDisable() {
		cageManager.setCaseBlocks(Material.GLASS);
		if(beforeMaxSize != 0) {
			fileManager.getSkyConfigYML().set("params.wait.max-size", beforeMaxSize);
			try {
				fileManager.getSkyConfigYML().save(fileManager.getSkyConfig());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		super.onDisable();
	}
	
	public void meteo() {
		World world = Bukkit.getWorld("world");
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setGameRuleValue("doWeatherCycle", "false");
	    world.setTime(1000);
	    world.setThunderDuration(0);
        world.setWeatherDuration(0);
        world.setStorm(false);
        world.setThundering(false);
	}
	
	private void checkCages() {
		if(!cageLocations.enoughMinimumCages()) {
			System.out.println("Le nombre de cage est inférieur au nombre de joueurs minimum pour lancer le jeu");
			System.out.println("Nombre de cage : "+cageLocations.getCages().size());
			System.out.println("Nombre de joueur minimum : "+fileManager.getSkyConfigYML().getInt("params.wait.size-to-begin"));
			Bukkit.shutdown();
		}
		if(!cageLocations.enoughMaximumCages()) {
			System.out.println("Le nombre de joueur maximum est supérieur au nombre de cages disponibles");
			System.out.println("Nombre de cage : "+cageLocations.getCages().size());
			System.out.println("Nombre de joueur maximum : "+fileManager.getSkyConfigYML().getInt("params.wait.max-size"));
			System.out.println("Donc, le nouveau nombre de joueur maximum DURANT CETTE PARTIE passe à "+cageLocations.getCages().size());
			beforeMaxSize = fileManager.getSkyConfigYML().getInt("params.wait.max-size");
			fileManager.getSkyConfigYML().set("params.wait.max-size", cageLocations.getCages().size());
			try {
				fileManager.getSkyConfigYML().save(fileManager.getSkyConfig());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public FileManager getFileManager() {
		return fileManager;
	}
	
	
	public GameStateManager getStateManager() {
		return stateManager;
	}

	public CageLocations getCageLocations() {
		return cageLocations;
	}


	public Title getTitle() {
		return sendTitles;
	}


	public CageManager getCageManager() {
		return cageManager;
	}
	
	public ChestRefill getChestRefill() {
		return chestRefill;
	}
	public ItemsRandom getItemsRandom() {
		return itemsRandom;
	}
	
	public void checkWin() {
		skyWin.checkWin();
	}
	
	public KitManager getKitManager() {
		return kitManager;
	}
	
}
