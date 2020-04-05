package fr.simonviel.skywars.chests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.utils.FileManager;

public class ItemsRandom {
	

	private FileManager fileManager;
	private FileConfiguration skyChestsYML;

	private Map<Integer, Integer> levels = new HashMap<>();
	private Map<String, Integer> chanceSection = new HashMap<>();
	
	
	public ItemsRandom(main main){
		this.fileManager = main.getFileManager();
		this.skyChestsYML = fileManager.getSkyChestsYML();
	}
	

	public List<ItemStack> getFinalItemStacks(){
	
		loadLevels();
		loadChanceSections();
		
		List<ItemStack> finalItemsStacks = new ArrayList<>();
		List<List<String>> sectionsDone = new ArrayList<>();
		int maxChestItem = skyChestsYML.getInt("max-item-per-chest");
		
		List<String> randomBrutItems = null;
		
		while(finalItemsStacks.size() != maxChestItem) {
			randomBrutItems = getRandomSection();
			while(sectionsDone.contains(randomBrutItems)) {
				randomBrutItems = getRandomSection();
			}
			sectionsDone.add(randomBrutItems);
			
			boolean added = false;
			while(!added) {
				for(String brutItem : randomBrutItems) {
					int chance = getPourcentageFromString(brutItem);
					double pourcentage = Math.random() * 100;
					if(pourcentage < chance) {
						ItemStack i = getItemFromString(brutItem);
						finalItemsStacks.add(i);
						added = true;
						break;
					}
					
				}
			}
			added = false;
		}
		return finalItemsStacks;
	}
	
	
	private List<String> getRandomSection(){
		
		ConfigurationSection itemsSection = skyChestsYML.getConfigurationSection("items");
		List<String> sectionList = new ArrayList<>();
		List<String> brutItemsList = null;
		
		
		// put all sections into a list
		for(String section : itemsSection.getKeys(false)) {
			sectionList.add(section);
		}
		
		while(brutItemsList == null) {
			// get one of that section
			Random random = new Random();
			String randomSection = sectionList.get(random.nextInt(sectionList.size()));
		
			// regarding to chance
			double pourcentage = Math.random() * 100;
			// System.out.println("tentative avec "+randomSection);
		    // System.out.println("cad avec "+chanceSection.get(randomSection)+"% de chance");
			if(pourcentage < chanceSection.get(randomSection)) {
				brutItemsList = itemsSection.getStringList(randomSection);
			}
		}
		
//		System.out.println(brutItemsList);
		
		return brutItemsList;
		
	}
	
	
	private void loadLevels() {
		ConfigurationSection levelsSection = skyChestsYML.getConfigurationSection("levels");
		for(String level : levelsSection.getKeys(false)) {
			String[] parsedLevel = level.split("-");
			int levelNumero = Integer.parseInt(parsedLevel[1]);
			int levelPourcentage = levelsSection.getInt(level);
			levels.put(levelNumero, levelPourcentage);
		}
	}
	
	private void loadChanceSections() {
		ConfigurationSection chanceSections = skyChestsYML.getConfigurationSection("chances");
		for(String section : chanceSections.getKeys(false)) {
			int pourcentageSection = chanceSections.getInt(section);
			chanceSection.put(section, pourcentageSection);
		}
	}
	
	private ItemStack getItemFromString(String string) {
		String[] parsedString = string.split("-");
		
		int amount = 1;
		Material material = Material.getMaterial(parsedString[0]);
		if(parsedString[0].contains(":")) {
			String[] parsedMaterial = parsedString[0].split(":");
			amount = Integer.parseInt(parsedMaterial[1]);
			material = Material.getMaterial(parsedMaterial[0]);
		}
		
		Enchantment enchantment = null;
		int enchantmentLevel = -1;
		
		if(parsedString[0].contains(",")) {
			String[] parsedMaterial = parsedString[0].split(",");
			material = Material.getMaterial(parsedMaterial[0]);
			enchantment = Enchantment.getByName(parsedMaterial[1]);
			enchantmentLevel = Integer.parseInt(parsedMaterial[2]);
		}
		
		ItemStack i = new ItemStack(material, amount);
		ItemMeta iM = i.getItemMeta();
		if(enchantment != null && enchantmentLevel != -1) {
			iM.addEnchant(enchantment, enchantmentLevel, true);
			iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		i.setItemMeta(iM);
		return i;
	}
	private int getPourcentageFromString(String string) {
		String[] parsedString = string.split("-");
		int level = Integer.parseInt(parsedString[1]);
		int pourcentage = levels.get(level);
		return pourcentage;
	}


	
}

