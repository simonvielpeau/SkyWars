package fr.simonviel.skywars.chests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.simonviel.skywars.main;



public class ChestRefill {
	private main main;
	private ItemsRandom itemsRandom;
	public ChestRefill(main main) {
		this.main = main;
		this.itemsRandom = this.main.getItemsRandom();
	}
	
	public void refillAllChests() {
		for(Chunk chunk : Bukkit.getWorld("world").getLoadedChunks()) {
			for(BlockState bs : chunk.getTileEntities()) {
				if(bs instanceof Chest) {
					Chest coffre =  (Chest) bs.getBlock().getState();
					
					Inventory inv = coffre.getInventory();
					inv.clear();
					List <Integer> slots = new ArrayList<>();
					Random r = new Random();
					
					for(ItemStack it : itemsRandom.getFinalItemStacks()) {
						int slot = r.nextInt(inv.getSize());
						while(slots.contains(slot)) {
							slot = r.nextInt(inv.getSize());
						}
						slots.add(slot);
						inv.setItem(slot, it);
					}		
				}
			}
		}
	}
	
}