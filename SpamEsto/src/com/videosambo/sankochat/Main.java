package com.videosambo.sankochat;


import java.awt.TrayIcon.MessageType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;

/**
 * @author videosambo
 * 
 * onnikontiokorpi@gmail.com
 **/
public class Main extends JavaPlugin {
	
	public void onEnable() {
		saveDefaultConfig();
		getCommand("sankochat").setExecutor(new Commands());
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
	}
	
	public void onDisable() {
		ChatListener cl = new ChatListener();
		cl.clearMessages();
		saveConfig();
	}
}
