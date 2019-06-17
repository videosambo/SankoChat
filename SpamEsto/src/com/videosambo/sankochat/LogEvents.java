package com.videosambo.sankochat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LogEvents implements Listener {
	
	private Main plugin;
	private Messages messages;
	
	public LogEvents(Main plugin) {
		this.plugin = plugin;
		messages = new Messages(plugin);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		String msg = plugin.getConfig().getString("log-join-message").replaceAll("%player%", e.getPlayer().getName());
		plugin.logText(msg, ChatType.EVENT);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		String msg = plugin.getConfig().getString("log-leave-message").replaceAll("%player%", e.getPlayer().getName());
		plugin.logText(msg, ChatType.EVENT);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player player = (Player) e.getEntity();
		String msg;
		if (player.isDead()) {
			if (player.getKiller() instanceof Player) {
				msg = plugin.getConfig().getString("log-kill-message").replaceAll("%killer%", player.getKiller().getName()).replaceAll("%player%", player.getName());
			} else {
				msg = plugin.getConfig().getString("log-death-message").replaceAll("%player%", player.getName());
			}
			plugin.logText(msg, ChatType.EVENT);
		}
		
	}

}
