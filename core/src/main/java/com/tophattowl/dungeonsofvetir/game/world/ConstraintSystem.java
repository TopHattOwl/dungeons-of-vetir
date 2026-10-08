package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.system.TurnSystem;

/**
 * Answers {@code can(entity, capability)} by aggregating sources (current zone +
 * the entity's timed constraints), and ticks timed constraints at world turn end.
 * <p>
 * Both concerns live here deliberately: gating is per-entity and every actor is an
 * entity; ticking is a turn-end system.
 */
public class ConstraintSystem implements TurnSystem {

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

    @Override
    public void process(WorldContext context) {
        for (Entity entity : world.query(ConstraintComponent.class)) {
            entity.getComponent(ConstraintComponent.class).tick();
        }
    }
}
