package fr.simonviel.skywars.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.simonviel.skywars.main;

public class FileManager {

	private main main;
	private FileConfiguration skyMessageYML;
	private File skyConfig;
	private FileConfiguration skyConfigYML;
	private FileConfiguration skyChestsYML;
	private FileConfiguration skyKitsYML;
	private Title title;
	private String prefix;
	
	public FileManager(main main) {
		this.main = main;
		this.title = main.getTitle();
		if(!main.getDataFolder().exists()) {
			main.getDataFolder().mkdir();
		}
		createMessagesFile();
		createSkyConfigFile();
		createSkyChestsFile();
		createSkyKitsFile();
		prefix = skyMessageYML.getString("messages.prefix").replace("&", "§");
	}

	private void createMessagesFile(){
		Path messageFile = main.getDataFolder().toPath().resolve("SkyMessages.yml");
		if (!Files.exists(messageFile)) {
            main.saveResource("SkyMessages.yml", true);
        }
		skyMessageYML = new YamlConfiguration();
        try {
        	skyMessageYML.load(messageFile.toFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createSkyConfigFile() {
		Path configFile = main.getDataFolder().toPath().resolve("SkyConfig.yml");
		if (!Files.exists(configFile)) {
            main.saveResource("SkyConfig.yml", true);
        }
        skyConfigYML = new YamlConfiguration();
    
		skyConfig = configFile.toFile();
        try {
        	skyConfigYML.load(configFile.toFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createSkyChestsFile() {
		Path chestFile = main.getDataFolder().toPath().resolve("SkyChests.yml");
		if (!Files.exists(chestFile)) {
            main.saveResource("SkyChests.yml", true);
        }
        skyChestsYML = new YamlConfiguration();
        try {
        	skyChestsYML.load(chestFile.toFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createSkyKitsFile() {
		Path kitFile = main.getDataFolder().toPath().resolve("SkyKits.yml");
		if (!Files.exists(kitFile)) {
            main.saveResource("SkyKits.yml", true);
        }
        skyKitsYML = new YamlConfiguration();
        try {
			skyKitsYML.load(kitFile.toFile());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String getLine(String path) {
		String string = skyMessageYML.getString(path);
		string = string.replace("&", "§");
		
		return prefix + " " + string;
	}
	public String getLine(String path, Player player, Player killer) {
		String string = skyMessageYML.getString(path);
		string = string.replace("&", "§");
		string = string.replace("%player%", player.getName());
		if(killer != null) {
			string = string.replace("%killer%", killer.getName());
		}
		return prefix + " " + string;
	}
	public String getLine(String path, Player player, int timer, int size) {
		
		String string = skyMessageYML.getString(path);
		string = string.replace("&", "§");
	
		if(player != null) {
			string = string.replace("%player%", player.getName());
		}
		if(timer != -1) {
			string = string.replace("%timer%", timer+"");
		}
		if(size != -1) {
			string = string.replace("%size%", size+"");
		}

		string = string.replace("%maxsize%", skyConfigYML.getInt("params.wait.max-size")+"");
		
		return prefix + " " + string;
	}
	public List<String> getStringList(String path){
		List<String> list = new ArrayList<>();
		for(String strings : skyMessageYML.getStringList(path)) {
			strings = strings.replace("&", "§");
			list.add(strings);
		}
		return list;
	}
	
	public List<String> getStringList(String path, Player player){
		List<String> list = new ArrayList<>();
		for(String strings : skyMessageYML.getStringList(path)) {
			strings = strings.replace("&", "§");
			strings = strings.replace("%player%", player.getName());
			list.add(strings);
		}
		return list;
	}

	public String[] getTitle(String path) {
		String string = skyMessageYML.getString(path).replace("&", "§");
		String[] array = string.split(";");
		if(array.length != 2) return null;
		return array;
	}
	public String[] getTitle(String path, Player player, int timer) {
		String string = skyMessageYML.getString(path).replace("&", "§");
		if(player != null) string = string.replace("%player%", player.getName());
		if(timer != -1) string = string.replace("%timer%", timer+"");
		String[] array = string.split(";");
		if(array.length != 2) return null;
		return array;
	}
	
	public void sendTitle(Player cible, String path, int timer) {
		String string = skyMessageYML.getString(path).replace("&", "§");
		if(cible == null) return;
		if(timer != -1) string = string.replace("%timer%", timer+"");
		String[] array = string.split(";");
		if(array.length == 1) {
			title.sendTitle(cible, array[0], "", 20);
			return;
		}
		if(array.length != 2) return;
		title.sendTitle(cible, array[0], array[1], 20);
	}
	
	// location set
	public void setConfigLoc(Player player, String locationName) {
		double x = player.getLocation().getX();
		double y = player.getLocation().getY();
		double z = player.getLocation().getZ();
		double yaw = player.getLocation().getYaw();
		double pitch = player.getLocation().getPitch();
		skyConfigYML.set("localisations."+locationName, x + ","+y+","+z+","+yaw+","+pitch);
		try {
			skyConfigYML.save(skyConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Location getConfigLoc(String locationName) {
		String full = (String) skyConfigYML.get("localisations."+locationName);
		String[] parsedLoc = full.split(",");
		double x = Double.valueOf(parsedLoc[0]);
		double y = Double.valueOf(parsedLoc[1]);
		double z = Double.valueOf(parsedLoc[2]);
		double yaw = Double.valueOf(parsedLoc[3]);
		double pitch = Double.valueOf(parsedLoc[4]);
		Location loc = new Location(Bukkit.getWorld("world"), x,y,z,(float)yaw, (float)pitch);
		return loc;
	}

	public void addCage(Player player) {
		double x = player.getLocation().getX();
		double y = player.getLocation().getY();
		double z = player.getLocation().getZ();
		double yaw = player.getLocation().getYaw();
		double pitch = player.getLocation().getPitch();
		
		List<String> allCages = skyConfigYML.getStringList("localisations.skywars-cage");
		allCages.add(x + ","+y+","+z+","+yaw+","+pitch);
		skyConfigYML.set("localisations.skywars-cage", allCages);
		try {
			skyConfigYML.save(skyConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<Location> getCageList() {
		List<String> fullList = skyConfigYML.getStringList("localisations.skywars-cage");
		List<Location> cageList= new ArrayList<>();
		for(String full : fullList) {
			String[] parsedLoc = full.split(",");
			double x = Double.valueOf(parsedLoc[0]);
			double y = Double.valueOf(parsedLoc[1]);
			double z = Double.valueOf(parsedLoc[2]);
			double yaw = Double.valueOf(parsedLoc[3]);
			double pitch = Double.valueOf(parsedLoc[4]);
			Location loc = new Location(Bukkit.getWorld("world"), x,y,z,(float)yaw,(float)pitch);
			cageList.add(loc);
		}
		
		return cageList;
	}


	public FileConfiguration getMessageYML() {
		return skyMessageYML;
	}
	
	public FileConfiguration getSkyConfigYML() {
		return skyConfigYML;
	}
	
	public File getSkyConfig() {
		return skyConfig;
	}
	
	public FileConfiguration getSkyChestsYML() {
		return skyChestsYML;
	}
	
	
	public FileConfiguration getSkyKitsYML() {
		return skyKitsYML;
	}
	
	
	
}
