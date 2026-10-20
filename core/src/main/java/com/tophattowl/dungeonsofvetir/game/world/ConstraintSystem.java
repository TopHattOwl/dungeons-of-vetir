package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;

/**
 * Answers {@code can(entity, capability)} by combining live zone denials with the entity's
 * compiled {@link ConstraintComponent} (which the status system rebuilds). Ticking of timed
 * denials lives on status effects, not here.
 */
public class ConstraintSystem {

    private final GameWorld world;

    public ConstraintSystem(GameWorld world) {
        this.world = world;
    }

    /**
     * Whether the entity may currently use the capability.
     */
    public boolean can(Entity entity, Capability capability) {
        Place place = world.getCurrentPlace();
        if (place != null && ZoneConstraints.denies(place.id().kind(), capability)) {
            return false;
        }

        ConstraintComponent component = entity.getComponent(ConstraintComponent.class);
        return component == null || component.allows(capability);
    }
}
