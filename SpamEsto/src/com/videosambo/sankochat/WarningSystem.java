package com.videosambo.sankochat;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

/**
 * @author videosambo
 *
 *         onnikontiokorpi@gmail.com
 **/
public class WarningSystem {
	private final Main pl;
	// private final int warningCount;
	private int warningCap;
	private String punishmentCommand;

	private static HashMap<UUID, Integer> warnings = new HashMap<>();

	public WarningSystem(Main pl) {
		this.pl = pl;

		punishmentCommand = pl.getConfig().getString("punishment-command");
		warningCap = pl.getConfig().getInt("warning-cap");
	}

	public void addWarning(UUID playerID, String from) {
		if (warnings.containsKey(playerID)) {
			warnings.put(playerID, getWarningCount(playerID) + 1);
		} else {
			warnings.put(playerID, 0);
		}
		String warningMessage = pl.getConfig().getString("warning-received");
		Bukkit.getPlayer(playerID).sendMessage(warningMessage.replace("{0}", from).replace("{1}",
				Integer.toString(getWarningCount(playerID))).replace("{2}", Integer.toString(
						warningCap)));
		this.runPunishment(playerID);
	}

	public void runPunishment(UUID playerID) {
		if (getWarningCount(playerID) == warningCap) {
			String reason = pl.getMessages().getMessage("punishent-message").replace("{0}", Integer
					.toString(warningCap));
			String command = pl.getConfig().getString("punishment-command").replace("{0}",
					getPlayerName(playerID)).replace("{1}", reason);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			Bukkit.getConsoleSender().sendMessage("Command: " + command + "   Reason: " + reason);
			clearWarnings(playerID);
		}
	}

	public void runOwnWarningCommand(UUID playerID) {
		String playerName = getPlayerName(playerID);
		String reason = pl.getMessages().getMessage("punishent-message").replace("{0}", Integer
				.toString(warningCap));
		String command = pl.getConfig().getString("warning-command").replace("{0}", playerName)
				.replace("{1}", reason);
		Bukkit.dispatchCommand(pl.getServer().getConsoleSender(), command);
	}

	private void clearWarnings(UUID playerID) {
		warnings.remove(playerID);
	}

	public void clearAllWarnings() {
		warnings.clear();
	}

	// GETTERS AND SETTERS
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
