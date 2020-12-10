package fi.videosambo.sankochat.module;

import fi.videosambo.sankochat.Main;
import fi.videosambo.sankochat.cache.History;
import fi.videosambo.sankochat.cache.PlayerHistory;
import fi.videosambo.sankochat.violations.ViolationHandler;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.UUID;

public class ModuleManager {

    private Main plugin;

    public ModuleManager(Main plugin) {
        this.plugin = plugin;
    }

    public History getMessageHistory() {
        return plugin.getHistory().getMessageHistory();
    }

    public PlayerHistory getPlayerMessageHistory(UUID uuid) {
        for (PlayerHistory history : plugin.getHistory().getPlayerHistories()) {
            if (history.getPlayerUUID().equals(uuid))
                return history;
        }
        return null;
    }

    public ArrayList<PlayerHistory> getPlayerMessageHistories() {
        return plugin.getHistory().getPlayerHistories();
    }

    public FileConfiguration getPluginConfig() {
        return plugin.getConfig();
    }

    public FileConfiguration getMessages() {
        return plugin.getLang();
    }

    public FileConfiguration getEnabledModules() {
        return plugin.getEnabledModules();
    }

    public FileConfiguration getModuleConfig() {
        return plugin.getModuleConfig();
    }

    //I dont even know is there need for this
    public void savePluginConfig() {
        plugin.saveConfig();
    }

    public void saveMessages() {
        plugin.saveMessages();
    }

    public void saveEnabledModules() {
        plugin.saveEnabledModules();
    }

    public void saveModuleConfig() {
        plugin.saveModuleConfig();
    }

    public ViolationHandler getViolationHandler() {
        return plugin.getViolations();
    }

}
