package com.tophattowl.dungeonsofvetir.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonDefinition;
import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonGenerator;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionCatalog;
import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Place;
import com.tophattowl.dungeonsofvetir.game.world.PlaceKind;
import com.tophattowl.dungeonsofvetir.game.world.TileTheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DungeonGeneratorTest {

    private static DungeonGenerator generator() {
        DungeonDefinition definition = new DungeonDefinition(
            "test", "Test", SectionCatalog.defaultCatalog(),
            "test_surface", GeneratorId.AUTHORED, TileTheme.SURFACE
        );
        return new DungeonGenerator(definition, 999);
    }

    @Test
    void resolvePlace_IsDungeonFloorWithTheme() {
        Place place = generator().resolvePlace(3);

        assertEquals(PlaceKind.DUNGEON_FLOOR, place.kind());
        assertEquals(3, place.depth());
        assertEquals("test", place.id().key());
        assertTrue(place.isDungeonFloor());
        assertNotNull(place.theme());
        assertEquals(Level.DEFAULT_WIDTH, place.width());
        assertEquals(Level.DEFAULT_HEIGHT, place.height());
    }

    @Test
    void resolveSurfacePlace_IsDistinctFromFloors() {
        DungeonGenerator generator = generator();
        Place surface = generator.resolveSurfacePlace();

        assertEquals(PlaceKind.DUNGEON_SURFACE, surface.kind());
        assertEquals("test_surface", surface.id().key());
        assertEquals(TileTheme.SURFACE, surface.theme());
    }

    @Test
    void sectionsMapToGeneratorIds() {
        DungeonGenerator generator = generator();
        assertEquals(GeneratorId.CAVE, generator.resolve(1).section().generator());
        assertEquals(GeneratorId.ROOMS, generator.resolve(7).section().generator());
    }

    @Test
    void generate_UsesPlaceSize() {
        DungeonGenerator generator = generator();
        Place base = generator.resolvePlace(1);
        Place custom = new Place(
            base.id(), 40, 30, base.seed(), base.theme(), base.role()
        );

        Level level = generator.generate(custom);

        assertEquals(40, level.getWidth());
        assertEquals(30, level.getHeight());
    }
}
