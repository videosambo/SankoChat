package fi.videosambo.sankochat.cache;

import fi.videosambo.sankochat.Message;

import java.util.ArrayList;

public class History {

    /*
    This class will keep all player messages in cache for certain time

    Differences between History and PlayerHistory is that PlayerHistory is for and only player own chat history filtering,
    PlayerHistory removes object when its parent player will leave. History will contain all messages from Players and will not clear if player leaves.
     */

    private ArrayList<Message> messages;

    public History() {
        messages = new ArrayList<Message>();
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
}
