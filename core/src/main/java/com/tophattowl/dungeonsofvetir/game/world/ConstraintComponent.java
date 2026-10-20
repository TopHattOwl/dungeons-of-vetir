package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The compiled set of capability denials currently affecting an entity. Rebuilt from active
 * status effects by the status system; zone-derived denials are checked live and never stored.
 */
public class ConstraintComponent implements Component {

    private final List<Constraint> constraints = new ArrayList<>();

    /**
     * Replaces the whole set (the status system recompiles from scratch each time).
     */
    public void setAll(List<Constraint> newConstraints) {
        constraints.clear();
        constraints.addAll(newConstraints);
    }

    public void deny(Capability capability, String reason) {
        constraints.add(new Constraint(capability, reason));
    }

    public void clear() {
        constraints.clear();
    }

    public boolean isEmpty() {
        return constraints.isEmpty();
    }

    public List<Constraint> constraints() {
        return constraints;
    }

    public boolean allows(Capability capability) {
        for (Constraint constraint : constraints) {
            if (constraint.capability() == capability) {
                return false;
            }
        }
        return true;
    }
}
