package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;

/**
 * A biome type entered from a wilderness overworld tile: which generator produces
 * its terrain and how it looks. Content is registered later.
 */
public record BiomeDefinition(String key, String name, GeneratorId generator, TileTheme theme) {
}
