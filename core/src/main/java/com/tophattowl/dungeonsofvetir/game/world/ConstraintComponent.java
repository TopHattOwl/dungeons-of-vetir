package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Timed constraints carried by an entity (injuries, status effects). Ticked once
 * per world turn by {@link ConstraintSystem}. Zone-derived denials are not stored
 * here; they are derived from the current place.
 */
public class ConstraintComponent implements Component {

    private final List<Constraint> constraints = new ArrayList<>();

    public void add(Constraint constraint) {
        constraints.add(constraint);
    }

    public void clear() {
        constraints.clear();
    }

    public List<Constraint> constraints() {
        return constraints;
    }

    public boolean allows(Capability capability) {
        for (Constraint constraint : constraints) {
            if (constraint.capability() == capability && constraint.active()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ages every timed constraint by one turn, dropping expired ones.
     */
    public void tick() {
        for (int i = constraints.size() - 1; i >= 0; i--) {
            Constraint next = constraints.get(i).ticked();
            if (next == null) {
                constraints.remove(i);
            } else {
                constraints.set(i, next);
            }
        }
    }
}
