package fr.simonviel.skywars.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.simonviel.skywars.main;

public class FileManager {

	private main main;
	private File messageFile;
	private FileConfiguration messageYML;
	private File skyConfig;
	private FileConfiguration skyConfigYML;
	private File skyChests;
	private FileConfiguration skyChestsYML;
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
		prefix = messageYML.getString("messages.prefix").replace("&", "§");
	}

	private void createMessagesFile(){
		messageFile = new File(main.getDataFolder(), "messages.yml");
		if(!messageFile.exists()) {	
			try (InputStream in = main.class.getClassLoader().getResourceAsStream("messages.yml")) {
				Files.copy(in, this.messageFile.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setMessages(YamlConfiguration.loadConfiguration(this.messageFile));
	}
	
	private void createSkyConfigFile() {
		skyConfig = new File(main.getDataFolder(), "SkyConfig.yml");
		if(!skyConfig.exists()) {
			try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("SkyConfig.yml")) {
				Files.copy(in, this.skyConfig.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		skyConfigYML = YamlConfiguration.loadConfiguration(this.skyConfig);
	}
	
	private void createSkyChestsFile() {
		skyChests = new File(main.getDataFolder(), "SkyChests.yml");
		if(!skyChests.exists()) {
			try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("SkyChests.yml")) {
				Files.copy(in, this.skyChests.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		skyChestsYML = YamlConfiguration.loadConfiguration(this.skyChests);
	}
	
	
	public String getLine(String path) {
		String string = messageYML.getString(path);
		string = string.replace("&", "§");
		
		return prefix + " " + string;
	}
	public String getLine(String path, Player player, Player killer) {
		String string = messageYML.getString(path);
		string = string.replace("&", "§");
		string = string.replace("%player%", player.getName());
		if(killer != null) {
			string = string.replace("%killer%", killer.getName());
		}
		return prefix + " " + string;
	}
	public String getLine(String path, Player player, int timer, int size) {
		
		String string = messageYML.getString(path);
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
		for(String strings : messageYML.getStringList(path)) {
			strings = strings.replace("&", "§");
			list.add(strings);
		}
		return list;
	}
	

	public void reloadMessages(){ setMessages(YamlConfiguration.loadConfiguration(this.messageFile)); }
	
	public void setMessages(FileConfiguration messages) {
		this.messageYML = messages;
	}
	
	public FileConfiguration getMessageYML() {
		return messageYML;
	}
	
	public FileConfiguration getSkyConfigYML() {
		return skyConfigYML;
	}
	
	public File getSkyConfig() {
		return skyConfig;
	}
	public String[] getTitle(String path) {
		String string = messageYML.getString(path).replace("&", "§");
		String[] array = string.split(";");
		if(array.length != 2) return null;
		return array;
	}
	public String[] getTitle(String path, Player player, int timer) {
		String string = messageYML.getString(path).replace("&", "§");
		if(player != null) string = string.replace("%player%", player.getName());
		if(timer != -1) string = string.replace("%timer%", timer+"");
		String[] array = string.split(";");
		if(array.length != 2) return null;
		return array;
	}
	
	public void sendTitle(Player cible, String path, int timer) {
		String string = messageYML.getString(path).replace("&", "§");
		if(cible == null) return;
		if(timer != -1) string = string.replace("%timer%", timer+"");
		String[] array = string.split(";");
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

	public FileConfiguration getSkyChestsYML() {
		return skyChestsYML;
	}
	
	public File getSkyChests() {
		return skyChests;
	}
	
	
}
