package com.videosambo.sankochat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

	private final Main pl;
	private final Messages messages;

	public CommandHandler(Main pl) {
		this.pl = pl;
		this.messages = pl.getMessages();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§8-===- §cSankoChat §7Commands §8-===-");
			sender.sendMessage("§7/sankochat reload §f- Reload config file");
			sender.sendMessage("§7/sankochat resetdata §f- Reset warnings and checks");
			return true;
		}

		// Must have args > 0
		if (args[0].equalsIgnoreCase("reload")) {
			pl.reloadConfig();
			sender.sendMessage("SankoChat has been reloaded");
		} else if (args[0].equalsIgnoreCase("resetdata")) {
			pl.getChatListener().clearMessages();
			pl.getWarningSystem().clearAllWarnings();
			sender.sendMessage("all warnings and check has been reseted");
		}

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				if (player.hasPermission("sankochat.command.reload")) {
					pl.reloadConfig();
					sender.sendMessage(messages.getMessage("reload-message"));

				} else {
					sender.sendMessage(messages.getMessage("no-permission"));
				}
				return true;
			} else if (args.length == 1 && args[0].equalsIgnoreCase("resetdata")) {
				if (!player.hasPermission("sankochat.command.resetdata")) {
					sender.sendMessage(messages.getMessage("no-permission"));
					return true;
				}
				pl.getChatListener().clearMessages();
				pl.getWarningSystem().clearAllWarnings();
				sender.sendMessage(messages.getMessage("datareset-message"));
			}
			return true;
		}

		return true;
	}

}
