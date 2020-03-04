package com.videosambo.sankochat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CommandListener implements Listener {

	private Main plugin = null;
	private Messages messages = null;
	private WarningSystem warning = null;

	private String noPermMessage;

	private HashMap<UUID, Integer> cooldownMap = new HashMap<UUID, Integer>();
	private int cooldownTime;
	private ArrayList<UUID> cooldown = new ArrayList<UUID>();
	private ArrayList<String> blockedCommands;

	public CommandListener(Main plugin) {
		this.plugin = plugin;
		messages = new Messages(plugin);
		warning = new WarningSystem(plugin);
		noPermMessage = messages.getMessage("no-permission", true);

		cooldownTime = plugin.getConfig().getInt("command-cooldown-time");
		blockedCommands = (ArrayList<String>) plugin.getConfig().getStringList("blocked-commands");
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPreprocessCommand(PlayerCommandPreprocessEvent e) {
		
		Player player = (Player) e.getPlayer();
		UUID playerID = player.getUniqueId();

		if (plugin.getConfig().getBoolean("log-commands"))
			plugin.logText("(" + e.getPlayer().getName() + ") " + e.getMessage(), ChatType.CMD);
		
		// Command typo cooldown
		if (plugin.getConfig().getBoolean("use-command-cooldown-time")) {
			if (!player.hasPermission("sankochat.bypass.cooldown.command")) {
				if (cooldown.contains(playerID)) {
					e.setCancelled(true);
					if (plugin.getConfig().getBoolean("enable-command-cooldown-message")) player.sendMessage(messages.getMessage("command-cooldown-message", true).replace("{0}",
							Integer.toString(cooldownMap.get(playerID))));
					if (plugin.getConfig().getBoolean("resend-null-message")) {
						e.setMessage(null);
					} else {
						if (plugin.getConfig().getBoolean("enable-resend-message"))
							e.setMessage(messages.getMessage("resend-message", false).replace("{0}", e.getMessage()));
					}
					return;
				} else {
					cooldownMap.put(playerID, plugin.getConfig().getInt("command-cooldown-time"));
					cooldown.add(playerID);
					new BukkitRunnable() {
						@Override
						public void run() {
							if (cooldownMap.get(playerID) <= 0) {
								cooldown.remove(playerID);
								this.cancel();
								return;
							}
							int time = cooldownMap.get(playerID);
							time--;
							cooldownMap.put(playerID, time);
						}
					}.runTaskTimerAsynchronously(plugin, 0, 20);
				}
			}
		}

		// Command blacklist
		if (plugin.getConfig().getBoolean("use-command-blacklist")) {
			if (!player.hasPermission("sankochat.bypass.blockedcommands")) {
				for (String blockedCMD : blockedCommands) {
					if (blockedCMD.startsWith("/")) {
						blockedCMD.substring(1);
					}
					if (e.getMessage().equalsIgnoreCase(blockedCMD)) {
						e.setCancelled(true);

						if (plugin.getConfig().getBoolean("enable-blocked-command-message"))
							player.sendMessage(messages.getMessage("blocked-command-message", true));

						if (plugin.getConfig().getBoolean("use-warning-system")) {
							if (plugin.getConfig().getBoolean("warn-blocked-commands")) {
								if (plugin.getConfig().getBoolean("use-own-warning-system")) {
									warning.runOwnWarningCommand(playerID);
								} else {
									String from = messages.getMessage("reason-blocked-commands", false);
									warning.addWarning(playerID, from);
								}
							}
						}

						if (plugin.getConfig().getBoolean("resend-null-message")) {
							e.setMessage(null);
						} else {
							if (plugin.getConfig().getBoolean("enable-resend-message"))
								e.setMessage(
										messages.getMessage("resend-message", true).replace("{0}", e.getMessage()));
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onConsoleCommand(ServerCommandEvent e) {
		if (plugin.getConfig().getBoolean("log-commands"))
			plugin.logText("(CONSOLE) " + e.getCommand(), ChatType.CMD);
	}

}
