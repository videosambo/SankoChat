package fi.videosambo.sankochat;

import java.util.UUID;

public class Message {

    /*
    Simple class used by History.class and PlayerHistory.class for keeping player uuid and string in cache

    Message.class will not contain timestamp of the message, only player uuid and message. I dont know if this is good choise but I can allways rewrite code if it could done better.
     */

    private UUID uuid;
    private String msg;
    private String originalMsg;
    private long timestamp;

    public Message(UUID uuid, String msg, long timestamp) {
        this.uuid = uuid;
        this.msg = msg;
        this.originalMsg = msg;
        this.timestamp = timestamp;
    }

    //Getters & Setters

    public UUID getUuid() {
        return uuid;
    }

    public String getMsg() {
        return msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOriginalMsg() {
        return originalMsg;
    }
}
