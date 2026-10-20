package com.tophattowl.dungeonsofvetir.game.world;

/**
 * A compiled denial of a {@link Capability} for an entity.
 * <p>
 * Denials are not timed here: the duration lives on the status effect that produced them, and
 * the status system recompiles this set whenever effects change. Zone denials are not stored
 * here (they are checked live from the current place).
 */
public record Constraint(Capability capability, String reason) {

    public static Constraint of(Capability capability, String reason) {
        return new Constraint(capability, reason);
    }
}
