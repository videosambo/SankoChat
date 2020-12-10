package fi.videosambo.sankochat;

import fi.videosambo.sankochat.cache.HistoryEvents;
import fi.videosambo.sankochat.cache.HistoryHandler;
import fi.videosambo.sankochat.module.ModuleHandler;
import fi.videosambo.sankochat.module.ModuleManager;
import fi.videosambo.sankochat.violations.ViolationHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Main extends JavaPlugin {

    private int clearCacheInterval;
    private Main instance;

    //Config files
    private File messagesFile = new File(this.getDataFolder()+"/messages.yml");
    private FileConfiguration messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    private File modulesFile = new File(this.getDataFolder()+"/modules.yml");
    private FileConfiguration modulesConfig = YamlConfiguration.loadConfiguration(modulesFile);
    private File moduleCfgFile = new File(this.getDataFolder()+"/module-config.yml");
    private FileConfiguration moduleCfgConfig = YamlConfiguration.loadConfiguration(moduleCfgFile);

    private ModuleManager moduleManager;
    private ModuleHandler modules;
    private HistoryHandler history;
    private ViolationHandler violations;

    @Override
    public void onEnable() {
        instance = this;
        initFiles();

        moduleManager = new ModuleManager(instance);
        modules = new ModuleHandler(instance);
        history = new HistoryHandler(instance);
        violations = new ViolationHandler(instance);
        //Events
        getServer().getPluginManager().registerEvents(new MessageEvent(instance), this);
        getServer().getPluginManager().registerEvents(new HistoryEvents(instance), this);

        //Runnables
        clearCacheInterval = getConfig().getInt("cache-clear-time");
        history.runTaskTimer(this, 0, 120*clearCacheInterval);
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

        if (!messagesFile.exists()) {
            this.saveResource("messages.yml",false);
        }
        if (!modulesFile.exists()) {
            this.saveResource("modules.yml",false);
        }
        if (!moduleCfgFile.exists()) {
            this.saveResource("module-config.yml",false);
        }
        File cfg = new File(this.getDataFolder(), "config.yml");

        if (!cfg.exists()) {
            saveResource("config.yml",false);
        }
    }

    @Override
    public void onDisable() {
        getModuleHandler().getPluginManager().stopPlugins();
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
        saveConfig(messagesConfig, messagesFile);
    }

    public void saveEnabledModules() {
        saveConfig(modulesConfig, modulesFile);
    }

    public void saveModuleConfig() {
        saveConfig(moduleCfgConfig, moduleCfgFile);
    }

    //Getters & Setters

    public FileConfiguration getLang() {
        return messagesConfig;
    }

    public FileConfiguration getEnabledModules() {
        return modulesConfig;
    }

    public FileConfiguration getModuleConfig() {
        return moduleCfgConfig;
    }

    public Main getInstance() {
        return instance;
    }


    public ModuleHandler getModuleHandler() {
        return modules;
    }

    public HistoryHandler getHistory() {
        return history;
    }

    public ViolationHandler getViolations() {
        return violations;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
