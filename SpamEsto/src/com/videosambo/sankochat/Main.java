package com.videosambo.sankochat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author videosambo
 * 
 *         onnikontiokorpi@gmail.com
 **/
public class Main extends JavaPlugin {

	private static Main instance = null;
	private Messages messages = null;
	private ChatListener listener = null;
	private WarningSystem warnings = null;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		getCommand("sankochat").setExecutor(new Commands());
		messages = new Messages(instance);
		listener = new ChatListener(instance);
		warnings = new WarningSystem(instance);
		getServer().getPluginManager().registerEvents(listener, this);
		autoClear();
	}

	public void onDisable() {
		listener.clearMessages();
	}
	
	public void runCommand(String command) {
		getServer().dispatchCommand(getServer().getConsoleSender(), command);
	}
	
	private void autoClear() {
		if (getConfig().getBoolean("clear-message-history") || getConfig().getBoolean("clear-warnings")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (getConfig().getBoolean("clear-message-history")) {
						listener.clearMessages();
					}
					if (getConfig().getBoolean("clear-warnings")) {
						warnings.clearAllWarnings();
					}
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						if (p.hasPermission("sankochat.notifications")) {
							p.sendMessage(messages.getMessage("clear-time-message", true));
						}
					}
					if (getConfig().getBoolean("show-console-message")) {
						Bukkit.getServer().getConsoleSender().sendMessage(messages.getMessage("clear-time-message", true));
					}
				}
			}.runTaskTimer(this, 0, (20 * 60 * getConfig().getInt("clear-time")));
		}
	}
	
	//GETTERIT JA SETTERIT
	
	public static Main getPluginInstance() {
		return instance;
	}

}
