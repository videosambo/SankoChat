package fi.videosambo.sankochat;

import fi.videosambo.sankochat.module.ChatFilter;
import fi.videosambo.sankochat.module.Module;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MessageEvent implements Listener {

    private Handler handler;

    public MessageEvent(Handler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent e) {
        //First we make new Message object that we want to pass to filters
        Message msg = new Message(e.getPlayer().getUniqueId(), e.getMessage(), System.currentTimeMillis());
        //Then we check if player has message history, if not we will add a new message and filter msg
        if (handler.getHistory().getPlayerHistory(msg.getUuid()).getMessages().isEmpty()) {
            //If the message history is empty, we will add message to history now and do all the filters that does not require player history
            handler.getHistory().addMessage(msg);
            for (ChatFilter module : handler.getModuleHandler().getModules()) {
                if (((Module) module).needsHistory())
                    continue;
                msg = module.filterMessage(msg);
                if (handler.getViolations().checkViolations(msg.getUuid()))
                    e.setCancelled(!(module instanceof Module) || ((Module) module).isViolationPolicy());
                //TODO tarkistetaan violationit ja suoritetaan toimet
            }
        } else {
            //If player has message history, then we can run all filters
            for (ChatFilter module : handler.getModuleHandler().getModules()) {
                msg = module.filterMessage(msg);
                if (handler.getViolations().checkViolations(msg.getUuid()))
                    e.setCancelled(!(module instanceof Module) || ((Module) module).isViolationPolicy());
                //TODO tarkistetaan violationit ja suoritetaan toimet
            }
            handler.getHistory().addMessage(msg);
        }
        e.setMessage(msg.getMsg());
    }

}
