package com.videosambo.sankochat;

/**
 * @author videosambo
 * 
 * onnikontiokorpi@gmail.com
 **/
public class Messages {

	Main plugin = Main.getPlugin(Main.class);

	public String getMessage(String message, boolean usePrefix) {

		String langMessage = plugin.getConfig().getString(message);

		if (plugin.getConfig().getBoolean("use-prefix")) {
			if (usePrefix)
				langMessage = plugin.getConfig().getString("prefix").toString() + langMessage;
		}

		langMessage = langMessage.replace("&", "§");

		return langMessage;
	}

}
