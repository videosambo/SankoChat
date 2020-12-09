package fi.videosambo.sankochat.cache;

import fi.videosambo.sankochat.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHistory {

    /*
    This class will keep cache of player unedited messages and timestamp for cachecleaner.
    Point of this class is to keep track of messages for filters.

    Differences between History and PlayerHistory is that PlayerHistory is for and only player own chat history filtering,
    PlayerHistory removes object when its parent player will leave. History will contain all messages from Players and will not clear if player leaves.
     */

    private UUID playerUUID;
    private ArrayList<Message> messages;

    public PlayerHistory(UUID playerUUID) {
        this.playerUUID = playerUUID;
        messages = new ArrayList<Message>();
    }

    public PlayerHistory(UUID playerUUID, Message m) {
        this.playerUUID = playerUUID;
        messages = new ArrayList<Message>();
        addNewMessage(m);
    }

    //Getters & Setters

    public void addNewMessage(Message message) {
        messages.add(message);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
