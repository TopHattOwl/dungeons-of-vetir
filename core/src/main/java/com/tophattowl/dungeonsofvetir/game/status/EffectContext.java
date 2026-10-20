package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.Random;

/**
 * Context handed to {@link EffectAction}s when they fire. Carries the world (so actions can
 * apply further statuses) and the RNG used by chance-based actions.
 */
public record EffectContext(GameWorld world, Random rng) {
}
