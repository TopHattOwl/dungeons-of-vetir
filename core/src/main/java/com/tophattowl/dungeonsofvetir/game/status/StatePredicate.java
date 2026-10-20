package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

/**
 * A condition on the entity's state. Evaluated each world turn: while it holds, the associated
 * state effect is present; when it stops holding, the effect is removed.
 */
@FunctionalInterface
public interface StatePredicate {
    boolean test(GameWorld world, Entity entity);
}
