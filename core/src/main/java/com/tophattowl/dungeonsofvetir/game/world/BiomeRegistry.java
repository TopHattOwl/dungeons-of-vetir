package com.tophattowl.dungeonsofvetir.game.world;

import java.util.HashMap;
import java.util.Map;

/**
 * Content table of biomes. Empty for now; biome content lands with the overworld.
 */
public final class BiomeRegistry {

    private static final Map<String, BiomeDefinition> biomes = new HashMap<>();

    private BiomeRegistry() {}

    public static void register(BiomeDefinition definition) {
        biomes.put(definition.key(), definition);
    }

    public static BiomeDefinition get(String key) {
        return biomes.get(key);
    }
}
