package fr.simonviel.skywars.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.simonviel.skywars.main;

public class CageManager {
	
	private main main;
	private List<Block> blocks = new ArrayList<>();
	public CageManager(main main) {
		this.main = main;
	}
	
	public void destroyAllCages() {
		setCaseBlocks(Material.AIR);
		
		new BukkitRunnable() {

			int timer = 2*2;
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				timer--;
				for(Player pls : main.getPlayers()) {
					pls.setHealth(pls.getMaxHealth());
				}
				if(timer == 0) {
					cancel();
				}
			}
			
			
		}.runTaskTimer(main, 0, 10);
	}
	
	public void cageInitializer() {
		for(Location cage : main.getFileManager().getCageList()) {
			for(int y = -4; y<=4; y++) {
				int min = -1;
				int max =1;
				for(int i = min; i <= max; i++) {
					Block nearBlock = Bukkit.getWorld("world").getBlockAt(cage.getBlockX()+i, cage.getBlockY()+y, cage.getBlockZ());
					if(nearBlock.getType() != Material.AIR && !blocks.contains(nearBlock)) blocks.add(nearBlock);
				}
				for(int i = min; i <= max; i++) {
					Block nearBlock = Bukkit.getWorld("world").getBlockAt(cage.getBlockX(), cage.getBlockY()+y, cage.getBlockZ()+i);
					if(nearBlock.getType() != Material.AIR && !blocks.contains(nearBlock)) blocks.add(nearBlock);
				}
				for(int i = min; i <= max; i++) {
					Block nearBlock = Bukkit.getWorld("world").getBlockAt(cage.getBlockX()+i, cage.getBlockY()+y, cage.getBlockZ()+i);
					if(nearBlock.getType() != Material.AIR && !blocks.contains(nearBlock)) blocks.add(nearBlock);
				}
				for(int i = min; i <= max; i++) {
					Block nearBlock = Bukkit.getWorld("world").getBlockAt(cage.getBlockX()+i, cage.getBlockY()+y, cage.getBlockZ()-i);
					if(nearBlock.getType() != Material.AIR && !blocks.contains(nearBlock)) blocks.add(nearBlock);
				}		
				for(int i = min; i <= max; i++) {
					Block nearBlock = Bukkit.getWorld("world").getBlockAt(cage.getBlockX()+i, cage.getBlockY()+y, cage.getBlockZ()+i+1);
					if(nearBlock.getType() != Material.AIR && !blocks.contains(nearBlock)) blocks.add(nearBlock);
				}
				for(int i = min; i <= max; i++) {
					Block nearBlock = Bukkit.getWorld("world").getBlockAt(cage.getBlockX()+i+1, cage.getBlockY()+y, cage.getBlockZ()+i);
					if(nearBlock.getType() != Material.AIR && !blocks.contains(nearBlock)) blocks.add(nearBlock);
				}
				for(int i = min; i <= max; i++) {
					Block nearBlock = Bukkit.getWorld("world").getBlockAt(cage.getBlockX()+i-1, cage.getBlockY()+y, cage.getBlockZ()+i+2);
					if(nearBlock.getType() != Material.AIR && !blocks.contains(nearBlock)) blocks.add(nearBlock);
				}
			}
		}
	}
	
	public void setCaseBlocks(Material material){
		for(Block block : blocks) {
			block.setType(material);
		}
	}
	
}
