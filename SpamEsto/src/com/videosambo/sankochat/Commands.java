package com.videosambo.sankochat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	Main plugin = Main.getPluginInstance();
	Messages messages = new Messages(plugin);
	ChatListener cl = new ChatListener(plugin);
	WarningSystem warning = new WarningSystem(plugin);

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(
						"�8-===- �cSankoChat �7Commands �8-===- �r\n" + "�7/sankochat reload �f- Reload config file�r\n"
								+ "�7/sankochat resetdata �f- Reset warnings and checks\n"
								+ "�8-===- �cV. �71.3.2 �8-===- �r");
			}

			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				if (player.hasPermission("sankochat.command.reload")) {
					plugin.reloadConfig();
					if (plugin.getConfig().getBoolean("enable-reload-message")) sender.sendMessage(messages.getMessage("reload-message", true));
					return true;
				} else {
					if (plugin.getConfig().getBoolean("enable-no-permission")) sender.sendMessage(messages.getMessage("no-permission", true));
					return true;
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("resetdata")) {
				if (player.hasPermission("sankochat.command.resetdata")) {
					cl.clearMessages();
					warning.clearAllWarnings();
					if (plugin.getConfig().getBoolean("enable-datareset-message")) sender.sendMessage(messages.getMessage("datareset-message", true));
					return true;
				} else {
					if (plugin.getConfig().getBoolean("enable-no-permission")) sender.sendMessage(messages.getMessage("no-permission", true));
					return true;
				} 
			}
		} else {
			if (args.length == 0) {
				Bukkit.getServer().getConsoleSender().sendMessage(
						"�8-===- �cSankoChat �7Commands �8-===- �r\n" + "�7/sankochat reload �f- Reload config file�r\n"
								+ "�7/sankochat resetdata �f- Reset warnings and checks\n"
								+ "�8-===- �cV. �71.3.2 �8-===- �r");
			}
			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				Bukkit.getServer().getConsoleSender().sendMessage("SankoChat has been reloaded");
				return true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("resetdata")) {
				cl.clearMessages();
				warning.clearAllWarnings();
				Bukkit.getServer().getConsoleSender().sendMessage("all warnings and check has been reseted");
				return true;

			}
		}

		return true;
	}

}
