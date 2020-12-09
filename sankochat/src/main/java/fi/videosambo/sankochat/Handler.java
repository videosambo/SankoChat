package fi.videosambo.sankochat;

import fi.videosambo.sankochat.cache.HistoryHandler;
import fi.videosambo.sankochat.module.ModuleHandler;
import fi.videosambo.sankochat.module.ModuleManager;
import fi.videosambo.sankochat.violations.ViolationHandler;
import org.bukkit.configuration.file.FileConfiguration;

public class Handler {

    private Main plugin;
    private ModuleManager moduleManager;
    private ModuleHandler modules;
    private HistoryHandler history;
    private ViolationHandler violations;

    public Handler(Main plugin) {
        this.plugin = plugin;
        moduleManager = new ModuleManager(this);
        modules = new ModuleHandler(this);
        history = new HistoryHandler(this);
        violations = new ViolationHandler(this);
    }

    public void saveConfig() {
        plugin.saveConfig();
    }

    public void saveMessages() {
        plugin.saveMessages();
    }

    public void saveEnabledModules() {
        plugin.saveModules();
    }

    public void saveModuleConfig() {
        plugin.saveModuleConfig();
    }

    //Getters & Setters

    public Main getPlugin() {
        return plugin;
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public FileConfiguration getLang() {
        return plugin.getLang();
    }

    public FileConfiguration getEnabledModules() {
        return plugin.getModules();
    }

    public FileConfiguration getModuleConfig() {
        return plugin.getModulesConfig();
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
