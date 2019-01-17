package com.videosambo.sankochat;

/**
 * @author videosambo
 * 
 * onnikontiokorpi@gmail.com
 **/
public class Messages {

	Main plugin = Main.getPlugin(Main.class);

	public String getMessage(String message) {

		String langMessage = plugin.getConfig().getString(message);

		if (plugin.getConfig().getBoolean("use-prefix")) {
			langMessage = plugin.getConfig().getString("prefix").toString() + langMessage;
		}

		langMessage = langMessage.replace("&", "§");

		return langMessage;
	}

}
