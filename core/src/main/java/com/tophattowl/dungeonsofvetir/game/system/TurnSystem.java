package com.tophattowl.dungeonsofvetir.game.system;

import com.tophattowl.dungeonsofvetir.game.world.WorldContext;

/**
 * A turn-boundary system: ordered, and run once per player turn via
 * {@link WorldContext#processTurn()}.
 * <p>
 * This is for things that must happen every turn in a defined order (status
 * effects, stamina, survival, encounters). It is NOT for on-demand action
 * resolution - those live in {@code game.action.resolvers}.
 */
public interface TurnSystem {
    void process(WorldContext context);
}
