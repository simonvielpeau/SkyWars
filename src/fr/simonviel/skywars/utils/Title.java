package fr.simonviel.skywars.utils;

import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle.EnumTitleAction;

public class Title {
	
	public void sendTitle(Player player, String title, String subTitle, int ticks) {
		
		IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
		IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + subTitle + "\"}");
		
		PacketPlayOutTitle titre = new PacketPlayOutTitle(EnumTitleAction.TITLE,chatTitle);
		PacketPlayOutTitle sousTitre = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,chatSubTitle);
		
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(titre);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(sousTitre);
		
		sendTime(player, ticks);
		
	}
	
	private void sendTime(Player player, int ticks) {
		PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, 20, ticks, 20);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
	}
	
	public void sendActionBar(Player player, String message) {
		
		IChatBaseComponent actionBar = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ab = new PacketPlayOutChat(actionBar, (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(ab);
		
	}

}
