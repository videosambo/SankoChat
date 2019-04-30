package com.videosambo.sankochat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author videosambo
 * 
 *         onnikontiokorpi@gmail.com
 **/
public class WarningSystem {

	Main plugin = null;
	Messages messages = null;
	Player player;

	private UUID playerID;
	private int warningCount;
	private int warningCap;
	private String punishmentCommand;

	private static HashMap<UUID, Integer> warnings = new HashMap<UUID, Integer>();

	public WarningSystem(Main instance) {
		this.plugin = instance;
		messages = new Messages(plugin);
		punishmentCommand = plugin.getConfig().getString("punishment-command");
		warningCap = plugin.getConfig().getInt("warning-cap");
	}

	public void addWarning(UUID playerID, String from) {
		this.playerID = playerID;
		if (warnings.containsKey(playerID)) {
			warnings.put(playerID, getWarningCount(playerID) + 1);
		} else {
			warnings.put(playerID, 1);
		}
		String warningMessage = plugin.getConfig().getString("warning-recived");
		getPlayer(playerID).sendMessage(messages.getMessage("warning-recived", true).replace("{0}", from)
				.replace("{1}", Integer.toString(getWarningCount(playerID)))
				.replace("{2}", Integer.toString(warningCap)));
		String message = messages.getMessage("warned-player", true).replace("{0}", getPlayerName(playerID))
				.replace("{1}", from).replace("{2}", Integer.toString(getWarningCount(playerID)))
				.replace("{3}", Integer.toString(warningCap));
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (p.hasPermission("sankochat.notifications")) {
				p.sendMessage(message);
			}
		}
		if (plugin.getConfig().getBoolean("show-console-message")) {
			Bukkit.getServer().getConsoleSender().sendMessage(message);
		}
		runPunishment(playerID);
	}

	public void runPunishment(UUID playerID) {
		if (getWarningCount(playerID) >= warningCap) {
			String reason = plugin.getConfig().getString("punishent-message").replace("{0}",
					Integer.toString(warningCap));
			String command = plugin.getConfig().getString("punishment-command").replace("{0}", getPlayerName(playerID))
					.replace("{1}", reason);
			plugin.runCommand(command);
			clearWarnings(playerID);
		}
	}

	public void runOwnWarningCommand(UUID playerID) {
		String playerName = getPlayerName(playerID);
		String reason = messages.getMessage("punishent-message", false).replace("{0}", Integer.toString(warningCap));
		String command = plugin.getConfig().getString("warning-command").replace("{0}", playerName).replace("{1}",
				reason);
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			public void run() {
				Bukkit.dispatchCommand(console, command);
			}
		});
	}

	private void clearWarnings(UUID playerID) {
		warnings.remove(playerID);
	}

	public void clearAllWarnings() {
		warnings.clear();
	}

	// GETTERS AND SETTERS

	public UUID getPlayerUUID() {
		return playerID;
	}

	public Player getPlayer(UUID playerID) {
		return Bukkit.getPlayer(playerID);
	}

	public String getPlayerName(UUID playerID) {
		return Bukkit.getPlayer(playerID).getName();
	}

	public int getWarningCount(UUID playerID) {
		return warnings.get(playerID);
	}

	public int getWarningCap() {
		return warningCap;
	}

	public void setWarningCap(int warningCap) {
		this.warningCap = warningCap;
	}

	public String getPunishmentCommand() {
		return punishmentCommand;
	}

	public void setPunishmentCommand(String punishmentCommand) {
		this.punishmentCommand = punishmentCommand;
	}

}
