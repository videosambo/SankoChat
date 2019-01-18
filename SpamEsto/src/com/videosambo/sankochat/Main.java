package com.videosambo.sankochat;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author videosambo
 * 
 *         onnikontiokorpi@gmail.com
 **/
public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();
		getCommand("sankochat").setExecutor(new Commands());
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
	}

	public void onDisable() {
		ChatListener cl = new ChatListener();
		cl.clearMessages();
	}
	

}
