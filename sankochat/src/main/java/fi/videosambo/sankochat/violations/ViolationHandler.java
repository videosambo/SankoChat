package fi.videosambo.sankochat.violations;

import fi.videosambo.sankochat.Handler;

import java.util.ArrayList;
import java.util.UUID;

public class ViolationHandler {

    private Handler handler;
    private ArrayList<Violation> violations;

    public ViolationHandler(Handler handler) {
        this.handler = handler;
        violations = new ArrayList<>();
    }

    public boolean checkViolations(UUID player) {
        for (Violation violation : violations) {
            if (violation.getPlayer().equals(player)) {
                violation.runPunishment();
                violations.remove(violation);
                return true;
            }
        }
        return false;
    }

    public void addViolation(Violation violation) {
        violations.add(violation);
    }

    public void addViolation(UUID player, String reason) {
        violations.add(new Violation(player, reason));
    }

    public void addViolation(UUID player, String reason, ViolationState state) {
        Violation violation = new Violation(player,reason);
        violation.setState(state);
        violations.add(violation);
    }
}
