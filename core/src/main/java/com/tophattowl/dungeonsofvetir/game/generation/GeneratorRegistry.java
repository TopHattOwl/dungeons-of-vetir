package com.tophattowl.dungeonsofvetir.game.generation;

import com.tophattowl.dungeonsofvetir.game.generation.generators.AuthoredLevelGenerator;
import com.tophattowl.dungeonsofvetir.game.generation.generators.CaveGenerator;
import com.tophattowl.dungeonsofvetir.game.generation.generators.RoomGenerator;
import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;

import java.util.EnumMap;
import java.util.Map;

/**
 * Content table mapping a {@link GeneratorId} to the generator that builds it.
 * Adding a biome or authored zone is one enum value plus one entry here.
 */
public final class GeneratorRegistry {

    private static final Map<GeneratorId, LevelGenerator> generators = new EnumMap<>(GeneratorId.class);

    static {
        generators.put(GeneratorId.CAVE, new CaveGenerator());
        generators.put(GeneratorId.ROOMS, new RoomGenerator());
        generators.put(GeneratorId.AUTHORED, new AuthoredLevelGenerator());
    }

    private GeneratorRegistry() {}

    public static LevelGenerator get(GeneratorId id) {
        LevelGenerator generator = generators.get(id);
        if (generator == null) {
            throw new IllegalStateException("No LevelGenerator registered for " + id);
        }
        return generator;
    }
}
