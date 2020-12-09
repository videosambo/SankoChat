package fi.videosambo.sankochat.antispammodule;

import fi.videosambo.sankochat.Main;
import fi.videosambo.sankochat.Message;
import fi.videosambo.sankochat.module.ChatFilter;
import fi.videosambo.sankochat.module.Module;
import fi.videosambo.sankochat.module.ModuleManager;
import fi.videosambo.sankochat.violations.ViolationState;
import org.pf4j.DefaultPluginDescriptor;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class AntiSpamModule extends Plugin {

    public AntiSpamModule(PluginWrapper wrapper) {
        super(wrapper);
    }
    /*
    start and stop methods that runs when SankoChat plugin runs onEnable and onDisable

    You can put enable messages here or init something if you want
    */
    @Override
    public void start() {}

    @Override
    public void stop() {}

    //This is filter part of module

    @Extension
    public static class AntiSpam extends Module implements ChatFilter {

        private HashMap<UUID, Integer> violationCount;

        @Override
        public void init() {
            //We will set priority here as a option
            setPriority(900);

            if (getModuleManager() == null) {
                System.out.println("ModuleManager not found");
                return;
            }

            //Checking does modules.yml have AntiSpam configuration fields
            if (!moduleManager.getModuleConfig().contains("antispam.message-interval"))
                moduleManager.getModuleConfig().set("antispam.message-interval", 3);
            if (!moduleManager.getModuleConfig().contains("antispam.violation-cap"))
                moduleManager.getModuleConfig().set("antispam.violation-cap", 5);
            if (!moduleManager.getModuleConfig().contains("antispam.cooldown-time"))
                moduleManager.getModuleConfig().set("antispam.cooldown-time", 30);
            moduleManager.saveModuleConfig();
            //Setting messages
            if (!moduleManager.getMessages().contains("antispam.dont-spam"))
                moduleManager.getMessages().set("antispam.dont-spam", "Don't send so many messages!");
            moduleManager.saveMessages();

            violationCount = new HashMap<>();
        }

        //filterMessage method that is the core part of filter, takes and returns Message object
        public Message filterMessage(Message message) {
            System.out.println(moduleManager.getPlayerMessageHistory(message.getUuid()).getMessages().size());
            long latestTimestamp = moduleManager.getPlayerMessageHistory(message.getUuid()).getMessages().get(moduleManager.getPlayerMessageHistory(message.getUuid()).getMessages().size()).getTimestamp();
            if ((message.getTimestamp() - latestTimestamp) < (1000 * moduleManager.getModuleConfig().getInt("antispam.message-interval"))) {
                if (!containsPlayerViolations(message.getUuid()))
                    violationCount.put(message.getUuid(), 0);
                else
                    violationCount.put(message.getUuid(),violationCount.get(message.getUuid()) + 1);
            }
            if (violationCount.get(message.getUuid()) >= moduleManager.getModuleConfig().getInt("antispam.violation-cap")) {
                moduleManager.getViolationHandler().addViolation(message.getUuid(), moduleManager.getMessages().getString("antispam.dont-spam"), ViolationState.WARN);
            }
            return message;
        }

        private boolean containsPlayerViolations(UUID uuid) {
            if (violationCount.containsKey(uuid))
                return true;
            return false;
        }
    }
}
