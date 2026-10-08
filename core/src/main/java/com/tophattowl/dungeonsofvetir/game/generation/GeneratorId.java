package com.tophattowl.dungeonsofvetir.game.generation;

/**
 * Selects which {@code LevelGenerator} builds a zone. One value per generation
 * shape/theme; adding a biome or authored zone is one value here plus a generator.
 * <p>
 * Subterranean generators (CAVE, ROOMS) are used by dungeon floors and cave-like
 * side rooms; the rest are surface/overworld styles.
 */
public enum GeneratorId {
    // subterranean (dungeon floors, cave-like side rooms)
    CAVE,
    ROOMS,

    // authored content (overworld, settlements, dungeon surfaces, side rooms)
    AUTHORED,

    // overworld biomes (content later)
    BIOME_FOREST,
    BIOME_FIELDS,
    BIOME_DESERT,

    // overworld / settlements (content later)
    SETTLEMENT,
    SURFACE,
    OVERWORLD,
}
