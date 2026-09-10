package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.world.Level;

/**
 * Produces a {@link Level} for a single floor
 * <p>
 * Implementations define a shape
 * (cave, rooms, open field) - the section/variation decides which one is used.
 */
public interface LevelGenerator {
    Level generate(GenerationContext ctx);
}
