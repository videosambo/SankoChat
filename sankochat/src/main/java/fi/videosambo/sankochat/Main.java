package fi.videosambo.sankochat;

import fi.videosambo.sankochat.cache.HistoryEvents;
import fi.videosambo.sankochat.module.ModuleManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    private Handler handler;
    private int clearCacheInterval;

    //Config files
    private File messages = new File(this.getDataFolder()+"/messages.yml");
    private FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messages);
    private File modules = new File(this.getDataFolder()+"/modules.yml");
    private FileConfiguration modulesConfig = YamlConfiguration.loadConfiguration(modules);
    private File moduleCfg = new File(this.getDataFolder()+"/module-config.yml");
    private FileConfiguration moduleCfgConfig = YamlConfiguration.loadConfiguration(modules);

    @Override
    public void onEnable() {
        initFiles();
        this.handler = new Handler(this);
        //Events
        getServer().getPluginManager().registerEvents(new MessageEvent(handler), this);
        getServer().getPluginManager().registerEvents(new HistoryEvents(handler), this);

        //Runnables
        clearCacheInterval = getConfig().getInt("cache-clear-time");
        handler.getHistory().runTaskTimer(this, 0, 120*clearCacheInterval);
    }

    private void initFiles() {
        if(!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        File mainDir = new File(this.getDataFolder()+"/modules");
        try {
            mainDir.mkdir();
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage("There was an error while creating folder " + this.getDataFolder()+"/modules/" + ": " + e.getLocalizedMessage());
            e.printStackTrace();
        }

        if (!messages.exists()) {
            this.saveResource("messages.yml",false);
        }
        if (!modules.exists()) {
            this.saveResource("modules.yml",false);
        }
        if (!moduleCfg.exists()) {
            this.saveResource("module-config.yml",false);
        }
        File cfg = new File(this.getDataFolder(), "config.yml");

        if (!cfg.exists()) {
            saveResource("config.yml",false);
        }
    }

    @Override
    public void onDisable() {
        handler.getModuleHandler().getPluginManager().stopPlugins();
    }

    //General save method
    private void saveConfig(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch(IOException e) {
            getServer().getConsoleSender().sendMessage("There was an error while saving file " + ymlFile + ": " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    //Config file saves
    public void saveMessages() {
        saveConfig(messagesConfig, messages);
    }

    public void saveModules() {
        saveConfig(modulesConfig, modules);
    }

    public void saveModuleConfig() {
        saveConfig(moduleCfgConfig, moduleCfg);
    }

    //Getters & Setters

    public FileConfiguration getLang() {
        return messagesConfig;
    }

    public FileConfiguration getModules() {
        return modulesConfig;
    }

    public FileConfiguration getModulesConfig() {
        return moduleCfgConfig;
    }
}
