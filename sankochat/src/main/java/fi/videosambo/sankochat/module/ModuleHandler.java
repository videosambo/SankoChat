package fi.videosambo.sankochat.module;

import fi.videosambo.sankochat.Handler;
import org.bukkit.Bukkit;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import java.nio.file.Paths;
import java.sql.Wrapper;
import java.util.Arrays;
import java.util.List;

public class ModuleHandler {

    private Handler handler;
    private PluginManager manager;

    private List<ChatFilter> modules;

    public ModuleHandler(Handler handler) {
        this.handler = handler;
        //Here we create new PluginManager and load available modules from modules folder
        manager = new DefaultPluginManager(Paths.get(handler.getPlugin().getDataFolder() + "/modules"));
        manager.loadPlugins();
        //Before starting modules we want to check if they are enabled or not
        for (PluginWrapper plugin : manager.getPlugins()) {
            //If modules section does not have a configuration for plugin, it is probably a new one so we want to add new configuration
            if (!handler.getEnabledModules().contains("enabled-modules." + plugin.getPluginId())) {
                handler.getEnabledModules().set("enabled-modules." + plugin.getPluginId(), true);
                handler.saveEnabledModules();
            }
            //If module has a configuration, we will check do we want to enable or disable the plugin
            //Module is already loaded so we only need to check if we need to disable it
            if (!handler.getEnabledModules().getBoolean("enabled-modules." + plugin.getPluginId())) {
                manager.unloadPlugin(plugin.getPluginId());
            }
        }
        //Now that we have unloaded all the modules that we dont need, we start all the modules that we need
        manager.startPlugins();
        //Every module should have ChatFilter extension that parses messages when used
        modules = manager.getExtensions(ChatFilter.class);
        Bukkit.getServer().getLogger().info(String.valueOf(modules.size())+" Module(s) found");

        for (ChatFilter module : modules) {
            if (handler.getModuleManager() == null) {
                System.out.println("MITÄ VITTUA");
            }
            ((Module) module).setModuleManager(handler.getModuleManager());
            module.init();
        }

        //TODO ei toimi koska vitun paska
        //Arrays.sort(new Module[]{(Module) modules});

    }

    //Getters & Setters

    public PluginManager getPluginManager() {
        return manager;
    }

    public List<ChatFilter> getModules() {
        return modules;
    }
}
