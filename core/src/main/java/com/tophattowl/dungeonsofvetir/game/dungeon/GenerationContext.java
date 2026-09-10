package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.section.ResolvedFloor;

/**
 * Everything a {@link LevelGenerator} needs to build one floor deterministically
 */
public record GenerationContext(
    long seed,
    int floorNumber,
    ResolvedFloor resolved
) {
}
