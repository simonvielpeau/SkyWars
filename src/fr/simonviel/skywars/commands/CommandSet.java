package fr.simonviel.skywars.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.simonviel.skywars.main;
import fr.simonviel.skywars.utils.FileManager;

public class CommandSet implements CommandExecutor {
	
	private main main;
	private FileManager fileManager;

	public CommandSet(main main) {
		this.main = main;
		this.fileManager = this.main.getFileManager();
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) return false;
		
		Player player = (Player) sender;
		if(label.equalsIgnoreCase("set")) {
			
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("skywars") && args[1].equalsIgnoreCase("addcage")) {
					fileManager.addCage(player);
					player.sendMessage(fileManager.getLine("messages.commands.set.success-cage"));
					return true;
				}
				player.sendMessage(fileManager.getStringList("messages.commands.set.help").get(0));
				player.sendMessage(fileManager.getStringList("messages.commands.set.help").get(1));
				return true;
			}
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("glasscage")) {
					main.getCageManager().setCaseBlocks(Material.GLASS);
					return true;
				}
				
				fileManager.setConfigLoc(player, args[0].toLowerCase());
				player.sendMessage(fileManager.getLine("messages.commands.set.success"));
				return true;
			}
			player.sendMessage(fileManager.getStringList("messages.commands.set.help").get(0));
			player.sendMessage(fileManager.getStringList("messages.commands.set.help").get(1));
			return true;
			
		}
		
		
		return false;
	}

}
