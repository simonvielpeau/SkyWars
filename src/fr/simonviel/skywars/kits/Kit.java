package fr.simonviel.skywars.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kit {
	
	private int slot;
	private ItemStack totem;
	private int cost;
	private String permission;
	private List<ItemStack> items;
	private List<Player> players = new ArrayList<>();
	
	public Kit(ConfigurationSection section) {
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
			
			String effectstring = null;
			String[] splitPotion = string.split("-");
			if(splitPotion[0].equalsIgnoreCase("potion")|| splitPotion[0].equalsIgnoreCase("splash_potion")) {
				string = splitPotion[0];
				effectstring = splitPotion[1];
			}
			
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
			if(itemSection.isSet("item-flags")) {
				for(String brutFlag : itemSection.getStringList("item-flags")) {
					ItemFlag flag = ItemFlag.valueOf(brutFlag);
					iM.addItemFlags(flag);
				}
			}
			if(itemSection.isSet("lore")) {
				List<String> lores = new ArrayList<>();
				for(String lore : itemSection.getStringList("lore")) {
					lores.add(lore.replace("&", "§"));
				}
				iM.setLore(lores);
			}
			if(string.equalsIgnoreCase("potion") || string.equalsIgnoreCase("splash_potion")) {
				PotionEffectType effect = PotionEffectType.getByName(effectstring);
				int level = itemSection.getInt("level");
				int duration = (int) (itemSection.getDouble("duration") * 1200);
				PotionMeta potionM = (PotionMeta) iM;
				potionM.addCustomEffect(new PotionEffect(effect, duration, level), true);
	
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
		for(String string : section.getRoot().getStringList("inventory.totem-lore.default")) {
			if(string.contains("%cost%")) {
				if(section.getInt("cost") == 0) {
					string = string.replace("%cost%", section.getRoot().getString("inventory.totem-lore.free-item").replace("&", "§"));
				}
				else {
					string = string.replace("%cost%", cost+"");
				}
			}
			if(section.isSet("permission")) {
				if(string.contains("%grade%")) {
					String permission = section.getString("permission").split("\\.")[1];
					string = string.replace("%permissionset%", "");
					string = string.replace("%grade%", permission);
				}
			}
			else if(string.contains("%grade%")) {
				string = null;
			}

			if(string != null)totemLore.add(string.replace("&", "§"));
		}
		totemM.setLore(totemLore);
		if(section.getString("totem").equals("POTION") || section.getString("totem").equals("SPLASH_POTION")) {
			totemM.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		}
		totemM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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
	
	public void addPlayer(Player player) throws Exception{
//		if(permission != null && player.hasPermission(permission)) {
			players.add(player);
//			return;
//		}
		// quand je l'installerai, déjà cc, et surtout -> il est possible de ne pas avoir la perm ET de pas avoir assez d'argent ! 
//		else if(permission != null) {
//			throw KitExceptions("permissions");
//		}
//		throw KitExceptions("coins");
	}
	
	
	public Exception KitExceptions(String string) {
		return new Exception(string);
	}

	public void removePlayer(Player player){
		players.remove(player);
	}

}
