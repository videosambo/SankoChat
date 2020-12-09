package fi.videosambo.sankochat.module;

import fi.videosambo.sankochat.Handler;
import fi.videosambo.sankochat.cache.History;
import fi.videosambo.sankochat.cache.PlayerHistory;
import fi.videosambo.sankochat.violations.ViolationHandler;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.UUID;

public class ModuleManager {

    private Handler handler;

    public ModuleManager(Handler handler) {
        this.handler = handler;
    }

    public History getMessageHistory() {
        return handler.getHistory().getMessageHistory();
    }

    public PlayerHistory getPlayerMessageHistory(UUID uuid) {
        for (PlayerHistory history : handler.getHistory().getPlayerHistories()) {
            if (history.getPlayerUUID().equals(uuid))
                return history;
        }
        return null;
    }

    public ArrayList<PlayerHistory> getPlayerMessageHistories() {
        return handler.getHistory().getPlayerHistories();
    }

    public FileConfiguration getPluginConfig() {
        return handler.getConfig();
    }

    public FileConfiguration getMessages() {
        return handler.getLang();
    }

    public FileConfiguration getEnabledModules() {
        return handler.getEnabledModules();
    }

    public FileConfiguration getModuleConfig() {
        return handler.getModuleConfig();
    }

    //I dont even know is there need for this
    public void savePluginConfig() {
        handler.saveConfig();
    }

    public void saveMessages() {
        handler.saveMessages();
    }

    public void saveEnabledModules() {
        handler.saveEnabledModules();
    }

    public void saveModuleConfig() {
        handler.saveModuleConfig();
    }

    public ViolationHandler getViolationHandler() {
        return handler.getViolations();
    }

}
