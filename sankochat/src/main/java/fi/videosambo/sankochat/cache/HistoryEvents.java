package fi.videosambo.sankochat.cache;

import fi.videosambo.sankochat.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class HistoryEvents implements Listener {

    private Main plugin;

    public HistoryEvents(Main plugin) {
        this.plugin = plugin;
    }

    //JoinEvent for adding player to history cache
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!plugin.getHistory().containsPlayerHistory(e.getPlayer().getUniqueId()))
            plugin.getHistory().addPlayerHistory(new PlayerHistory(e.getPlayer().getUniqueId()));
    }

    public void onQuit(PlayerQuitEvent e) {
        if (plugin.getHistory().containsPlayerHistory(e.getPlayer().getUniqueId()))
            plugin.getHistory().removePlayerHisotory(e.getPlayer().getUniqueId());
    }
}
