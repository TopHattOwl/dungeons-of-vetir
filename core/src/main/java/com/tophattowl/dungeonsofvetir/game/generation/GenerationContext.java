package com.tophattowl.dungeonsofvetir.game.generation;

/**
 * Everything a {@link LevelGenerator} needs to build one zone. Deliberately does
 * not carry section/variation info - the generator is already selected for the
 * place, it just needs the size and seed.
 */
public record GenerationContext(
    long seed,
    int floorNumber,
    int width,
    int height,
    String zoneKey
) {
}
