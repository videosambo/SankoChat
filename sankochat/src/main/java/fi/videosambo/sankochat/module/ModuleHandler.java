package fi.videosambo.sankochat.module;

import fi.videosambo.sankochat.Main;
import org.bukkit.Bukkit;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import java.nio.file.Paths;
import java.util.List;

public class ModuleHandler {

    private Main plugin;
    private PluginManager manager;

    private List<ChatFilter> modules;

    public ModuleHandler(Main plugin) {
        this.plugin = plugin;
        //Here we create new PluginManager and load available modules from modules folder
        manager = new DefaultPluginManager(Paths.get(plugin.getDataFolder() + "/modules"));
        manager.loadPlugins();
        //Before starting modules we want to check if they are enabled or not
        for (PluginWrapper pl : manager.getPlugins()) {
            //If modules section does not have a configuration for plugin, it is probably a new one so we want to add new configuration
            if (!plugin.getEnabledModules().contains("enabled-modules." + pl.getPluginId())) {
                plugin.getEnabledModules().set("enabled-modules." + pl.getPluginId(), true);
                plugin.saveEnabledModules();
            }
            //If module has a configuration, we will check do we want to enable or disable the plugin
            //Module is already loaded so we only need to check if we need to disable it
            if (!plugin.getEnabledModules().getBoolean("enabled-modules." + pl.getPluginId())) {
                manager.unloadPlugin(pl.getPluginId());
            }
        }
        //Now that we have unloaded all the modules that we dont need, we start all the modules that we need
        manager.startPlugins();
        //Every module should have ChatFilter extension that parses messages when used
        modules = manager.getExtensions(ChatFilter.class);
        Bukkit.getServer().getLogger().info(String.valueOf(modules.size())+" Module(s) found");

        for (ChatFilter module : modules) {
            if (plugin.getModuleManager() == null) {
                System.out.println("MITÄ VITTUA");
            }
            ((Module) module).setModuleManager(plugin.getModuleManager());
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
