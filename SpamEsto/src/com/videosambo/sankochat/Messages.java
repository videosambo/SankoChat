package com.videosambo.sankochat;

import org.bukkit.ChatColor;

/**
 * @author videosambo
 *
 *         onnikontiokorpi@gmail.com
 **/
public class Messages {
	private final Main pl;

	public Messages(Main pl) {
		this.pl = pl;
	}

	public String getMessage(String message) {
		String langMessage = pl.getConfig().getString(message);

		if (pl.getConfig().getBoolean("use-prefix"))
			langMessage = pl.getConfig().getString("prefix") + langMessage;

		return ChatColor.translateAlternateColorCodes('&', langMessage);
	}

}
