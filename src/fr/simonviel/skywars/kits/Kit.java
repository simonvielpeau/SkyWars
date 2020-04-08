package fr.simonviel.skywars.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import fr.simonviel.skywars.utils.FileManager;

public class Kit {
	
	private FileManager fileManager;
	private int slot;
	private ItemStack totem;
	private int cost;
	private String permission;
	private List<ItemStack> items;
	private List<Player> players = new ArrayList<>();
	
	public Kit(ConfigurationSection section, FileManager fileManager) {
		this.fileManager = fileManager;
		this.cost = section.getInt("cost");
		
		this.totem = getTotem(section);
		
		if(section.isSet("permission")) {
			this.permission = section.getString("permission");
		}
		items = getItems(section.getConfigurationSection("items"));
	}
	
	
	private List<ItemStack> getItems(ConfigurationSection section){
		List<ItemStack> items = new ArrayList<>();
		
		for(String string : section.getKeys(false)) {
			ConfigurationSection itemSection = section.getConfigurationSection(string);
			int amount = 1;
			if(itemSection.isSet("amount")) {
				amount = itemSection.getInt("amount");
			}
			Material material = Material.getMaterial(string.toUpperCase());
			ItemStack i = new ItemStack(material, amount);
			ItemMeta iM = i.getItemMeta();
			if(itemSection.isSet("name")) {
				String name = itemSection.getString("name").replace("&", "§");
				iM.setDisplayName(name);
			}
			if(itemSection.isSet("enchantments")) {
				for(String brutEnchantment : itemSection.getStringList("enchantments")) {
					Enchantment enchantment = Enchantment.getByName(brutEnchantment.split(":")[0]);
					
					iM.addEnchant(enchantment, Integer.parseInt(brutEnchantment.split(":")[1]), true);				
				}
			}
			if(itemSection.isSet("lore")) {
				List<String> lores = new ArrayList<>();
				for(String lore : itemSection.getStringList("lore")) {
					lores.add(lore.replace("&", "§"));
				}
				iM.setLore(lores);
			}
			if(itemSection.toString().equalsIgnoreCase("potion") || itemSection.toString().equalsIgnoreCase("splash_potion")) {
				PotionType effect = PotionType.valueOf(itemSection.getString("effect"));
				PotionMeta potionM = (PotionMeta) iM;
				potionM.setBasePotionData(new PotionData(effect, false, true));
				i.setItemMeta(potionM);
			}
			i.setItemMeta(iM);
			items.add(i);
		}
		return items;
	}

	public ItemStack getTotem(ConfigurationSection section) {
		String totemString = section.getString("totem").toUpperCase();
		ItemStack totem = new ItemStack(Material.getMaterial(totemString),1);
		ItemMeta totemM = totem.getItemMeta();
		totemM.setDisplayName(section.getString("totem-name").replace("&", "§"));
		List<String> totemLore = new ArrayList<>();
		for(String string : section.getStringList("totem-lore")) {	
			totemLore.add(string.replace("&", "§"));
		}
		for(String string : fileManager.getSkyKitsYML().getStringList("inventory.default-totem-lore")) {
			totemLore.add(string.replace("&", "§").replace("%cost%", cost+""));
		}
		totemM.setLore(totemLore);
		totem.setItemMeta(totemM);
		
		return totem;
	}

	public int getSlot() {
		return slot;
	}


	public void setSlot(int slot) {
		this.slot = slot;
	}


	public ItemStack getTotem() {
		return totem;
	}


	public String getPermission() {
		return permission;
	}


	public List<ItemStack> getItems() {
		return items;
	}


	public int getCost() {
		return cost;
	}
	
	public boolean containsPlayer(Player player) {
		return players.contains(player);
	}
	
	public void addPlayer(Player player){
		players.add(player);
	}
	
	public void removePlayer(Player player){
		players.remove(player);
	}

}
