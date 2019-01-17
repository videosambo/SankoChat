package com.videosambo.sankochat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

	Main plugin = Main.getPlugin(Main.class);
	Messages messages = new Messages();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				if (player.hasPermission("sankochat.command.reload")) {
					plugin.reloadConfig();
					plugin.saveConfig();
					sender.sendMessage(messages.getMessage("reload-message"));
					return true;
				} else {
					sender.sendMessage(messages.getMessage("no-permission"));
					return true;
				}
			}
		} else {
			plugin.reloadConfig();
			plugin.saveConfig();
			return true;
		}

		return false;
	}

}
