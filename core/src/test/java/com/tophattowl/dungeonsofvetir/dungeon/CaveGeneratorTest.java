package com.tophattowl.dungeonsofvetir.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.GenerationContext;
import com.tophattowl.dungeonsofvetir.game.dungeon.generators.CaveGenerator;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.ResolvedFloor;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionCatalog;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.WorldLayout;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaveGeneratorTest {

    private static GenerationContext ctx(long worldSeed, int floor) {
        WorldLayout layout = new WorldLayout(worldSeed, SectionCatalog.defaultCatalog());
        ResolvedFloor resolved = layout.resolve(floor);
        return new GenerationContext(resolved.seed(), floor, resolved);
    }

    private static Level generate(long worldSeed, int floor) {
        return new CaveGenerator().generate(ctx(worldSeed, floor));
    }

    @Test
    void generate_ReturnsLevel() {
        assertNotNull(generate(12345, 1));
    }

    @Test
    void generate_SetsCorrectFloorNumber() {
        assertEquals(5, generate(12345, 5).floorNumber);
    }

    @Test
    void generate_HasFloorTiles() {
        Level level = generate(12345, 1);

        boolean hasFloor = false;
        outer:
        for (int x = 1; x < Level.WIDTH - 1; x++) {
            for (int y = 1; y < Level.HEIGHT - 1; y++) {
                if (level.getTile(x, y).type == TileType.FLOOR) {
                    hasFloor = true;
                    break outer;
                }
            }
        }
        assertTrue(hasFloor, "Generated level should have at least one floor tile");
    }

    @Test
    void generate_HasWallTiles() {
        Level level = generate(12345, 1);
        assertNotNull(level.getTile(0, 0));
        assertEquals(TileType.WALL, level.getTile(0, 0).type);
    }

    @Test
    void generate_HasStairs() {
        Level level = generate(12345, 1);

        int up = 0;
        int down = 0;
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                if (level.getTile(x, y).type == TileType.STAIRS_UP) up++;
                if (level.getTile(x, y).type == TileType.STAIRS_DOWN) down++;
            }
        }
        assertEquals(1, up, "Should have exactly one stairs up");
        assertEquals(1, down, "Should have exactly one stairs down");
    }

    @Test
    void generate_Deterministic_SameSeed() {
        Level level1 = generate(12345, 1);
        Level level2 = generate(12345, 1);

        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                assertEquals(level1.getTile(x, y).type, level2.getTile(x, y).type,
                    "Tiles should match at (" + x + "," + y + ")");
            }
        }
    }

    @Test
    void generate_DifferentSeeds_DifferentDungeons() {
        Level level1 = generate(12345, 1);
        Level level2 = generate(54321, 1);

        boolean different = false;
        outer:
        for (int x = 0; x < Level.WIDTH && !different; x++) {
            for (int y = 0; y < Level.HEIGHT && !different; y++) {
                if (level1.getTile(x, y).type != level2.getTile(x, y).type) {
                    different = true;
                }
            }
        }
        assertTrue(different, "Different seeds should produce different dungeons");
    }

    @Test
    void generate_BordersAreWalls() {
        Level level = generate(12345, 1);

        for (int x = 0; x < Level.WIDTH; x++) {
            assertEquals(TileType.WALL, level.getTile(x, 0).type, "Top border should be wall");
            assertEquals(TileType.WALL, level.getTile(x, Level.HEIGHT - 1).type, "Bottom border should be wall");
        }
        for (int y = 0; y < Level.HEIGHT; y++) {
            assertEquals(TileType.WALL, level.getTile(0, y).type, "Left border should be wall");
            assertEquals(TileType.WALL, level.getTile(Level.WIDTH - 1, y).type, "Right border should be wall");
        }
    }
}
