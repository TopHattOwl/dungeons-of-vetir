package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.section.ResolvedFloor;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.WorldLayout;
import com.tophattowl.dungeonsofvetir.game.generation.GenerationContext;
import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;
import com.tophattowl.dungeonsofvetir.game.generation.GeneratorRegistry;
import com.tophattowl.dungeonsofvetir.game.generation.LevelGenerator;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Place;
import com.tophattowl.dungeonsofvetir.game.world.PlaceId;
import com.tophattowl.dungeonsofvetir.game.world.PlaceKind;
import com.tophattowl.dungeonsofvetir.game.world.PlaceRole;

/**
 * Resolves a dungeon's surface and floors to {@link Place}s and dispatches them to
 * the {@link GeneratorRegistry}. One instance per dungeon run.
 */
public class DungeonGenerator {

    private final DungeonDefinition definition;
    private final WorldLayout layout;
    private final long seed;

    public DungeonGenerator(DungeonDefinition definition, long seed) {
        this.definition = definition;
        this.seed = seed;
        this.layout = new WorldLayout(seed, definition.sections());
    }

    public String dungeonKey() {
        return definition.key();
    }

    public DungeonDefinition definition() {
        return definition;
    }

    public Level generateLevel(int floorNumber) {
        return generate(resolvePlace(floorNumber));
    }

    /**
     * Generates a dungeon zone (surface or floor). Surface uses the definition's
     * surface generator; floors use the section's generator.
     */
    public Level generate(Place place) {
        GeneratorId generatorId = switch (place.id().kind()) {
            case DUNGEON_SURFACE -> definition.surfaceGenerator();
            case DUNGEON_FLOOR -> layout.resolve(place.depth()).section().generator();
            default -> throw new IllegalArgumentException(
                "DungeonGenerator cannot build a " + place.id().kind());
        };

        LevelGenerator generator = GeneratorRegistry.get(generatorId);
        GenerationContext ctx = new GenerationContext(
            place.seed(), place.depth(), place.width(), place.height(), place.id().key()
        );
        return generator.generate(ctx);
    }

    /**
     * The dungeon's surface/entrance level (depth 0).
     */
    public Place resolveSurfacePlace() {
        return new Place(
            PlaceId.dungeonSurface(definition.surfaceKey()),
            Level.DEFAULT_WIDTH,
            Level.DEFAULT_HEIGHT,
            seed,
            definition.surfaceTheme(),
            PlaceRole.NORMAL
        );
    }

    /**
     * Resolves a dungeon floor to its {@link Place}, carrying the theme and role
     * so the display and gameplay can react without knowing about sections.
     */
    public Place resolvePlace(int floorNumber) {
        ResolvedFloor resolved = layout.resolve(floorNumber);
        return new Place(
            PlaceId.dungeonFloor(definition.key(), floorNumber),
            Level.DEFAULT_WIDTH,
            Level.DEFAULT_HEIGHT,
            resolved.seed(),
            resolved.theme(),
            resolved.role()
        );
    }

    public ResolvedFloor resolve(int floorNumber) {
        return layout.resolve(floorNumber);
    }

    /**
     * Whether the layout has a floor at this depth (1..maxFloor).
     */
    public boolean hasFloor(int floorNumber) {
        return floorNumber >= 1 && floorNumber <= layout.maxFloor();
    }

    public WorldLayout layout() {
        return layout;
    }
}
