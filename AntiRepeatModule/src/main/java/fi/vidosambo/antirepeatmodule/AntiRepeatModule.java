package fi.vidosambo.antirepeatmodule;

import fi.videosambo.sankochat.Message;
import fi.videosambo.sankochat.module.ChatFilter;
import fi.videosambo.sankochat.module.Module;
import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class AntiRepeatModule extends Plugin {

    public AntiRepeatModule(PluginWrapper wrapper) {
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
    public static class AntiRepeat extends Module implements ChatFilter {

        @Override
        public void init() {
            //We will set priority here as a option
            setPriority(800);

            //Checking does modules.yml have AntiRepeat configuration fields
            if (!moduleManager.getModuleConfig().contains("antirepeat.check-last-messages"))
                moduleManager.getModuleConfig().set("antirepeat.check-last-messages", 3);
            if (!moduleManager.getModuleConfig().contains("antirepeat.message-similarity"))
                moduleManager.getModuleConfig().set("antirepeat.message-similarity", 0.9);
            moduleManager.saveModuleConfig();
            //Setting messages
            if (!moduleManager.getMessages().contains("antispam.dont-repeat"))
                moduleManager.getMessages().set("antispam.dont-repeat", "Don't repeat yourself!");
            moduleManager.saveMessages();
        }

        //filterMessage method that is the core part of filter, takes and returns Message object
        public Message filterMessage(Message message) {
            return null;
        }
    }
}
