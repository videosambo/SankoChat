package fi.videosambo.sankochat.violations;

import java.util.UUID;

public class Violation {

    private UUID player;
    private String reason;
    private ViolationState state;

    public Violation(UUID player, String reason) {
        this.player = player;
        this.reason = reason;
    }

    public void runPunishment() {
        //TODO tee jotain kun pelaaja rikkoo
    }

    //Getters & Setters

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ViolationState getState() {
        return state;
    }

    public void setState(ViolationState state) {
        this.state = state;
    }
}
