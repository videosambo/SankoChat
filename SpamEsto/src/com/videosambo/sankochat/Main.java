package com.videosambo.sankochat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author videosambo
 * 
 *         onnikontiokorpi@gmail.com
 **/

enum ChatType {
	CMD, MSG, EVENT, NULL
}

public class Main extends JavaPlugin {

	public static final String VERSION = "1.3.3";
	
	private static Main instance = null;
	private Messages messages = null;
	private ChatListener listener = null;
	private CommandListener cmdListener = null;
	private LogEvents logEvents = null;
	private WarningSystem warnings = null;
	
	private File logFile;
	
	@Override
	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		getCommand("sankochat").setExecutor(new Commands());
		messages = new Messages(instance);
		logFile = new File(getDataFolder(), "log.txt");
		setupLog();
		listener = new ChatListener(instance);
		cmdListener = new CommandListener(instance);
		logEvents = new LogEvents(instance);
		warnings = new WarningSystem(instance);
		getServer().getPluginManager().registerEvents(listener, this);
		getServer().getPluginManager().registerEvents(cmdListener, this);
		getServer().getPluginManager().registerEvents(logEvents, this);
		autoClear();
		logText("===== SankoChat enabled =====", ChatType.NULL);
	}

	public void onDisable() {
		logText("===== SankoChat disabled =====", ChatType.NULL);
		listener.clearMessages();
	}
	
	public void runCommand(String command) {
		getServer().dispatchCommand(getServer().getConsoleSender(), command);
	}
	
	private void autoClear() {
		if (getConfig().getBoolean("clear-message-history") || getConfig().getBoolean("clear-warnings")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (getConfig().getBoolean("clear-message-history")) {
						listener.clearMessages();
					}
					if (getConfig().getBoolean("clear-warnings")) {
						warnings.clearAllWarnings();
					}
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						if (p.hasPermission("sankochat.notifications")) {
							if(getConfig().getBoolean("enable-clear-time-message")) p.sendMessage(messages.getMessage("clear-time-message", true));
						}
					}
					if (getConfig().getBoolean("show-console-message")) {
						if(getConfig().getBoolean("enable-clear-time-message")) Bukkit.getServer().getConsoleSender().sendMessage(messages.getMessage("clear-time-message", true));
					}
				}
			}.runTaskTimer(this, 0, (20 * 60 * getConfig().getInt("clear-time")));
		}
	}
	
	//Tiedostot
	
	private void setupLog() {
		
		if(!logFile.exists()){
            try {
            	logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
	}
	
	public void logText(String txt, ChatType type) {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy kk:mm:ss");
        String date = "[" + dateFormat.format(new Date()) + "] ";
		
        String fm;
        
        if (type == ChatType.CMD) {
        	fm = "[CMD] " + date + txt + "\n";
        } else if (type == ChatType.MSG){
        	fm = "[MSG] " + date + txt + "\n";
        } else if (type == ChatType.EVENT){
        	fm = "[EVENT] " + date + txt + "\n";
        } else {
        	fm = date + txt + "\n";
        }
        
		try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));
            bw.append(fm);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	//GETTERIT JA SETTERIT
	
	public static Main getPluginInstance() {
		return instance;
	}

}
