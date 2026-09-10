package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.generators.CaveGenerator;
import com.tophattowl.dungeonsofvetir.game.dungeon.generators.RoomGenerator;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.ResolvedFloor;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionId;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.WorldLayout;
import com.tophattowl.dungeonsofvetir.game.world.Level;

import java.util.EnumMap;
import java.util.Map;

/**
 * Orchestrates level generation: resolves a floor to its section/variation via
 * {@link WorldLayout} and dispatches to the {@link LevelGenerator} registered for
 * that section's shape
 */
public class DungeonGenerator {

    private final WorldLayout layout;
    private final Map<SectionId, LevelGenerator> generators = new EnumMap<>(SectionId.class);

    public DungeonGenerator(WorldLayout layout) {
        this.layout = layout;
        generators.put(SectionId.CAVES, new CaveGenerator());
        generators.put(SectionId.RUINS, new RoomGenerator());
    }

    public Level generateLevel(int floorNumber) {
        ResolvedFloor resolved = layout.resolve(floorNumber);

        LevelGenerator generator = generators.get(resolved.section().id());
        if (generator == null) {
            throw new IllegalStateException(
                "No LevelGenerator registered for section " + resolved.section().id()
            );
        }

        GenerationContext ctx = new GenerationContext(resolved.seed(), floorNumber, resolved);
        return generator.generate(ctx);
    }

    public ResolvedFloor resolve(int floorNumber) {
        return layout.resolve(floorNumber);
    }

    public WorldLayout layout() {
        return layout;
    }
}
