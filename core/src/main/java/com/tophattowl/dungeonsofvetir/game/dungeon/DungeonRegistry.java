package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionCatalog;
import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;
import com.tophattowl.dungeonsofvetir.game.world.TileTheme;

import java.util.HashMap;
import java.util.Map;

/**
 * Content table of dungeons keyed by their id. The overworld and quests reference
 * dungeons by key.
 */
public final class DungeonRegistry {

    public static final String DEFAULT_KEY = "vetir";

    private static final Map<String, DungeonDefinition> dungeons = new HashMap<>();

    static {
        register(new DungeonDefinition(
            DEFAULT_KEY,
            "Vetir Depths",
            SectionCatalog.defaultCatalog(),
            "vetir_surface",
            GeneratorId.AUTHORED,
            TileTheme.SURFACE
        ));
    }

    private DungeonRegistry() {}

    private static void register(DungeonDefinition definition) {
        dungeons.put(definition.key(), definition);
    }

    public static DungeonDefinition get(String key) {
        DungeonDefinition definition = dungeons.get(key);
        if (definition == null) {
            throw new IllegalArgumentException("No dungeon registered with key " + key);
        }
        return definition;
    }

    public static DungeonDefinition defaultDungeon() {
        return get(DEFAULT_KEY);
    }
}
