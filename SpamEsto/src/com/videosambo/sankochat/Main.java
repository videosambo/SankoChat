package com.videosambo.sankochat;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author videosambo
 *
 *         onnikontiokorpi@gmail.com
 **/
public class Main extends JavaPlugin {
	private final ChatListener chatListener;
	private final CommandHandler ch;

	public Main() {
		this.chatListener = new ChatListener(this);
		this.ch = new CommandHandler(this);
	}

	@Override
	public void onEnable() {
		saveDefaultConfig();

		getCommand("sankochat").setExecutor(ch);
		getServer().getPluginManager().registerEvents(chatListener, this);
	}

	@Override
	public void onDisable() {
		chatListener.clearMessages();
	}

	public ChatListener getChatListener() {
		return chatListener;
	}
}
