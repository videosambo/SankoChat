package com.videosambo.sankochat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author videosambo
 * 
 *         onnikontiokorpi@gmail.com
 **/
public class WarningSystem {

	Main plugin = Main.getPlugin(Main.class);
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	Messages messages = new Messages();
	Player player;

	private UUID playerID;
	private int warningCount;
	private int warningCap;
	private String punishmentCommand;

	private static HashMap<UUID, Integer> warnings = new HashMap<UUID, Integer>();

	public WarningSystem() {
		punishmentCommand = plugin.getConfig().getString("punishment-command");
		warningCap = plugin.getConfig().getInt("warning-cap");
	}

	public void addWarning(UUID playerID, String from) {
		this.playerID = playerID;
		if (warnings.containsKey(playerID)) {
			warnings.put(playerID, getWarningCount(playerID) + 1);
		} else {
			warnings.put(playerID, 0);
		}
		String warningMessage = plugin.getConfig().getString("warning-recived");
		getPlayer(playerID).sendMessage(messages.getMessage("warning-recived").replace("{0}", from)
				.replace("{1}", Integer.toString(getWarningCount(playerID)))
				.replace("{2}", Integer.toString(warningCap)));
		runPunishment(playerID);
	}

	public void runPunishment(UUID playerID) {
		if (getWarningCount(playerID) == warningCap) {
			String reason = messages.getMessage("punishent-message").replace("{0}", Integer.toString(warningCap));
			String command = plugin.getConfig().getString("punishment-command").replace("{0}", getPlayerName(playerID))
					.replace("{1}", reason);
			Bukkit.dispatchCommand(console, command);
			Bukkit.getConsoleSender().sendMessage("Command: " + command + "   Reason: " + reason);
			clearWarnings(playerID);
		}
	}

	public void runOwnWarningCommand(UUID playerID) {
		String playerName = getPlayerName(playerID);
		String reason = messages.getMessage("punishent-message").replace("{0}", Integer.toString(warningCap));
		String command = plugin.getConfig().getString("warning-command").replace("{0}", playerName).replace("{1}",
				reason);
		Bukkit.dispatchCommand(plugin.getServer().getConsoleSender(), command);
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
