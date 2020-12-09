package fi.videosambo.sankochat.cache;

import fi.videosambo.sankochat.Handler;
import fi.videosambo.sankochat.Message;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class HistoryHandler extends BukkitRunnable {

    /*
    This class will be handling all cached messages and will clear old cached messages.
     */

    private ArrayList<PlayerHistory> playerHistories;
    private History messageHistory;

    private Handler handler;

    private int messageCacheAge;

    public HistoryHandler(Handler handler) {
        this.handler = handler;
        playerHistories = new ArrayList<>();
        messageHistory = new History();
        messageCacheAge = handler.getConfig().getInt("cache-clear-age");
    }

    public void clearCache() {
        //First we clear PlayerHistory
        for (PlayerHistory history : playerHistories) {
            for (Message message : history.getMessages()) {
                long time = System.currentTimeMillis() - message.getTimestamp();
                if (time >= 600000) {
                    playerHistories.remove(message);
                }
            }
        }
        //Then we clear History
        for (Message message : messageHistory.getMessages()) {
            long time = System.currentTimeMillis() - message.getTimestamp();
            if (time >= 600000) {
                messageHistory.getMessages().remove(message);
            }
        }
    }

    @Override
    public void run() {
        clearCache();
    }

    public boolean containsPlayerHistory(UUID uuid) {
        for (PlayerHistory m : playerHistories) {
            if (m.getPlayerUUID().equals(uuid))
                return true;
        }
        return false;
    }

    private void addMessageToPlayer(Message message) {
        for (PlayerHistory m : playerHistories) {
            if (m.getPlayerUUID().equals(m.getPlayerUUID()))
                m.addNewMessage(message);
        }
    }

    public void removePlayerHisotory (UUID uuid) {
        for (PlayerHistory m : playerHistories) {
            if (m.getPlayerUUID().equals(uuid))
                playerHistories.remove(m);
        }
    }

    public PlayerHistory getPlayerHistory(UUID uuid) {
        for (PlayerHistory m : playerHistories) {
            if (m.getPlayerUUID().equals(uuid))
                return m;
        }
        return null;
    }

    //Getters & Setters

    public void addPlayerHistory(PlayerHistory h) {
        playerHistories.add(h);
    }

    public ArrayList<PlayerHistory> getPlayerHistories() {
        return playerHistories;
    }

    public History getMessageHistory() {
        return messageHistory;
    }

    public void addMessage(Message message) {
        //First we add message to MessageHistory
        messageHistory.addNewMessage(message);
        //Then we add message to player personal history
        if (!containsPlayerHistory(message.getUuid()))
            playerHistories.add(new PlayerHistory(message.getUuid(), message));
        else
            addMessageToPlayer(message);
    }
}
