package com.tophattowl.dungeonsofvetir.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.CellularAutomataGenerator;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellularAutomataGeneratorTest {

    @Test
    void generate_ReturnsLevel() {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(1);
        assertNotNull(level);
    }

    @Test
    void generate_SetsCorrectFloorNumber() {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(5);
        assertEquals(5, level.floorNumber);
    }

    @Test
    void generate_HasFloorTiles() {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(1);

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
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(1);
        assertNotNull(level.getTile(0, 0));
        assertEquals(TileType.WALL, level.getTile(0, 0).type);
    }

    @Test
    void generate_HasStairsUp() {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(1);

        boolean hasStairsUp = false;
        outer:
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                if (level.getTile(x, y).type == TileType.STAIRS_UP) {
                    hasStairsUp = true;
                    break outer;
                }
            }
        }
        assertTrue(hasStairsUp, "Generated level should have stairs up");
    }

    @Test
    void generate_HasStairsDown() {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(1);

        boolean hasStairsDown = false;
        outer:
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                if (level.getTile(x, y).type == TileType.STAIRS_DOWN) {
                    hasStairsDown = true;
                    break outer;
                }
            }
        }
        assertTrue(hasStairsDown, "Generated level should have stairs down");
    }

    @Test
    void generate_Deterministic_SameSeed() {
        CellularAutomataGenerator generator1 = new CellularAutomataGenerator(12345);
        CellularAutomataGenerator generator2 = new CellularAutomataGenerator(12345);

        Level level1 = generator1.generate(1);
        Level level2 = generator2.generate(1);

        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                assertEquals(level1.getTile(x, y).type, level2.getTile(x, y).type,
                    "Tiles should match at (" + x + "," + y + ")");
            }
        }
    }

    @Test
    void generate_DifferentSeeds_DifferentDungeons() {
        CellularAutomataGenerator generator1 = new CellularAutomataGenerator(12345);
        CellularAutomataGenerator generator2 = new CellularAutomataGenerator(54321);

        Level level1 = generator1.generate(1);
        Level level2 = generator2.generate(1);

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
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(1);

        for (int x = 0; x < Level.WIDTH; x++) {
            assertEquals(TileType.WALL, level.getTile(x, 0).type, "Top border should be wall");
            assertEquals(TileType.WALL, level.getTile(x, Level.HEIGHT - 1).type, "Bottom border should be wall");
        }
        for (int y = 0; y < Level.HEIGHT; y++) {
            assertEquals(TileType.WALL, level.getTile(0, y).type, "Left border should be wall");
            assertEquals(TileType.WALL, level.getTile(Level.WIDTH - 1, y).type, "Right border should be wall");
        }
    }

    @Test
    void generate_FloorAreaConnected() {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(1);

        int floorCount = 0;
        for (int x = 1; x < Level.WIDTH - 1; x++) {
            for (int y = 1; y < Level.HEIGHT - 1; y++) {
                if (level.getTile(x, y).type == TileType.FLOOR ||
                    level.getTile(x, y).type == TileType.STAIRS_UP ||
                    level.getTile(x, y).type == TileType.STAIRS_DOWN) {
                    floorCount++;
                }
            }
        }
        assertTrue(floorCount > 0, "Should have at least one walkable tile");
    }

    @Test
    void generate_StairsAreFloorTiles() {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(12345);
        Level level = generator.generate(1);

        int stairsUpCount = 0;
        int stairsDownCount = 0;
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                if (level.getTile(x, y).type == TileType.STAIRS_UP) stairsUpCount++;
                if (level.getTile(x, y).type == TileType.STAIRS_DOWN) stairsDownCount++;
            }
        }

        assertEquals(1, stairsUpCount, "Should have exactly one stairs up");
        assertEquals(1, stairsDownCount, "Should have exactly one stairs down");
    }
}
