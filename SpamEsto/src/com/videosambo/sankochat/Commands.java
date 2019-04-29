package com.videosambo.sankochat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	Main plugin = Main.getPlugin(Main.class);
	Messages messages = new Messages();
	ChatListener cl = new ChatListener();
	WarningSystem warning = new WarningSystem();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage(
						"§8-===- §cSankoChat §7Commands §8-===- §r\n" + "§7/sankochat reload §f- Reload config file§r\n"
								+ "§7/sankochat resetdata §f- Reset warnings and checks");
			}

			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				if (player.hasPermission("sankochat.command.reload")) {
					plugin.reloadConfig();
					sender.sendMessage(messages.getMessage("reload-message", true));
					return false;
				} else {
					sender.sendMessage(messages.getMessage("no-permission", true));
					return false;
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("resetdata")) {
				if (player.hasPermission("sankochat.command.resetdata")) {
					cl.clearMessages();
					warning.clearAllWarnings();
					sender.sendMessage(messages.getMessage("datareset-message", true));
					return false;
				} else {
					sender.sendMessage(messages.getMessage("no-permission", true));
					return false;
				}
			}
		} else {
			if (args.length == 0) {
				Bukkit.getServer().getConsoleSender().sendMessage(
						"§8-===- §cSankoChat §7Commands §8-===- §r\n" + "§7/sankochat reload §f- Reload config file§r\n"
								+ "§7/sankochat resetdata §f- Reset warnings and checks");
			}
			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				Bukkit.getServer().getConsoleSender().sendMessage("SankoChat has been reloaded");
				return false;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("resetdata")) {
				cl.clearMessages();
				warning.clearAllWarnings();
				Bukkit.getServer().getConsoleSender().sendMessage("all warnings and check has been reseted");
				return false;

			}
		}

		return true;
	}

}
