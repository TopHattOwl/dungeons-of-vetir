package com.tophattowl.dungeonsofvetir.game.world;

/**
 * A denial of a {@link Capability} from some source. {@code turnsRemaining < 0}
 * means permanent; otherwise it is a duration ticked down over turns.
 */
public record Constraint(Capability capability, String reason, int turnsRemaining) {

    public static Constraint permanent(Capability capability, String reason) {
        return new Constraint(capability, reason, -1);
    }

    public static Constraint forTurns(Capability capability, String reason, int turns) {
        return new Constraint(capability, reason, turns);
    }

    public boolean active() {
        return turnsRemaining != 0;
    }

    /**
     * @return this constraint one turn older, or {@code null} if it has expired
     */
    public Constraint ticked() {
        if (turnsRemaining <= 0) return this; // permanent
        int remaining = turnsRemaining - 1;
        return remaining == 0 ? null : new Constraint(capability, reason, remaining);
    }
}
