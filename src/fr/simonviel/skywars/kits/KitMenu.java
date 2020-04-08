package fr.simonviel.skywars.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.simonviel.skywars.main;

public class KitMenu implements Listener{
	
	private Inventory inv;
	private KitManager kitManager;
	private FileConfiguration skyKitsYML;
	
	public KitMenu(main main) {
		kitManager = main.getKitManager();
		skyKitsYML = main.getFileManager().getSkyKitsYML();
		
		inv = generateInv();
	}
	
	public Inventory generateInv() {
		Inventory inv = Bukkit.createInventory(null, 27, skyKitsYML.getString("inventory.name").replace("&", "§"));
		
		for(Kit kit : kitManager.getKits()) {
			inv.setItem(kit.getSlot(), kit.getTotem());
		}
		
		return inv;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		p.getInventory().clear();
		
		ItemStack i = new ItemStack(Material.getMaterial(skyKitsYML.getString("inventory.item.material")));
		ItemMeta iM = i.getItemMeta();
		iM.setDisplayName(skyKitsYML.getString("inventory.item.name").replace("&", "§"));
		List<String> lores = new ArrayList<>();
		for(String lore : skyKitsYML.getStringList("inventory.item.lore")) {
			lores.add(lore.replace("&", "§"));
		}
		iM.setLore(lores);
		i.setItemMeta(iM);
		
		p.getInventory().setItem(0, i);
		p.updateInventory();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		Player p = e.getPlayer();
		ItemStack i = e.getItem();
		
		Material material = Material.getMaterial(skyKitsYML.getString("inventory.item.material"));
		
		if(i != null && i.getType() != null && i.getType() == material) {
			p.openInventory(inv);
		}
		
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getWhoClicked() == null) return;
		Player p = (Player) e.getWhoClicked();
		Inventory inv = e.getInventory();
		ItemStack current = e.getCurrentItem();
		
		if(inv.getName() == skyKitsYML.getString("inventory.item.name").replace("&", "§")) {
			if(current == null) return;
			e.setCancelled(true);
			p.closeInventory();
		}
		
	}

}
