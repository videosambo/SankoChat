package fi.videosambo.sankochat.cache;

import fi.videosambo.sankochat.Handler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HistoryEvents implements Listener {

    private Handler handler;

    public HistoryEvents(Handler handler) {
        this.handler = handler;
    }

    //JoinEvent for adding player to history cache
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!handler.getHistory().containsPlayerHistory(e.getPlayer().getUniqueId()))
            handler.getHistory().addPlayerHistory(new PlayerHistory(e.getPlayer().getUniqueId()));
    }

    public void onQuit(PlayerQuitEvent e) {
        if (handler.getHistory().containsPlayerHistory(e.getPlayer().getUniqueId()))
            handler.getHistory().removePlayerHisotory(e.getPlayer().getUniqueId());
    }
}
