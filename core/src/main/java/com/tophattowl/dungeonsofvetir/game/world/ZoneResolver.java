package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonGenerator;
import com.tophattowl.dungeonsofvetir.game.generation.GenerationContext;
import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;
import com.tophattowl.dungeonsofvetir.game.generation.GeneratorRegistry;
import com.tophattowl.dungeonsofvetir.game.generation.LevelGenerator;

/**
 * Decides which generator builds a place, from its kind and definition. Dungeon
 * floors/surfaces go through the dungeon plan; everything else is looked up in the
 * relevant content registry. Single source of truth for generator selection.
 */
public class ZoneResolver {

    private final DungeonGenerator dungeonGenerator;

    public ZoneResolver(DungeonGenerator dungeonGenerator) {
        this.dungeonGenerator = dungeonGenerator;
    }

    public Level generate(Place place) {
        PlaceKind kind = place.id().kind();
        if (kind == PlaceKind.DUNGEON_FLOOR || kind == PlaceKind.DUNGEON_SURFACE) {
            return dungeonGenerator.generate(place);
        }
        return generateWith(generatorFor(place), place);
    }

    private GeneratorId generatorFor(Place place) {
        String key = place.id().templateKey();
        return switch (place.id().kind()) {
            case SIDE_ROOM -> {
                SideRoomDefinition def = SideRoomRegistry.get(key);
                yield def != null ? def.generator() : GeneratorId.AUTHORED;
            }
            case BIOME -> {
                BiomeDefinition def = BiomeRegistry.get(key);
                yield def != null ? def.generator() : GeneratorId.AUTHORED;
            }
            case SETTLEMENT -> {
                SettlementDefinition def = SettlementRegistry.get(key);
                yield def != null ? def.generator() : GeneratorId.AUTHORED;
            }
            case OVERWORLD -> GeneratorId.OVERWORLD;
            default -> throw new IllegalArgumentException("Unresolvable place kind " + place.id().kind());
        };
    }

    private Level generateWith(GeneratorId generatorId, Place place) {
        LevelGenerator generator = GeneratorRegistry.get(generatorId);
        GenerationContext ctx = new GenerationContext(
            place.seed(), place.depth(), place.width(), place.height(), place.id().templateKey()
        );
        return generator.generate(ctx);
    }
}
