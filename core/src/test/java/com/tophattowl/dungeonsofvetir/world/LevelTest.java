package com.tophattowl.dungeonsofvetir.world;

import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LevelTest {

    @Test
    void constructor_FillsWithWalls() {
        Level level = new Level(1);
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                assertEquals(TileType.WALL, level.getTile(x, y).type);
            }
        }
    }

    @Test
    void constructor_StoresFloorNumber() {
        Level level = new Level(5);
        assertEquals(5, level.floorNumber);
    }

    @Test
    void getTile_InBounds() {
        Level level = new Level(1);
        level.setTile(40, 25, TileType.FLOOR);
        assertEquals(TileType.FLOOR, level.getTile(40, 25).type);
    }

    @Test
    void getTile_OutOfBounds_NegativeX_ReturnsBorderWall() {
        Level level = new Level(1);
        assertEquals(TileType.BORDER_WALL, level.getTile(-1, 25).type);
    }

    @Test
    void getTile_OutOfBounds_NegativeY_ReturnsBorderWall() {
        Level level = new Level(1);
        assertEquals(TileType.BORDER_WALL, level.getTile(25, -1).type);
    }

    @Test
    void getTile_OutOfBounds_BothNegative_ReturnsBorderWall() {
        Level level = new Level(1);
        assertEquals(TileType.BORDER_WALL, level.getTile(-5, -5).type);
    }

    @Test
    void getTile_OutOfBounds_XTooLarge_ReturnsBorderWall() {
        Level level = new Level(1);
        assertEquals(TileType.BORDER_WALL, level.getTile(Level.WIDTH, 25).type);
    }

    @Test
    void getTile_OutOfBounds_YTooLarge_ReturnsBorderWall() {
        Level level = new Level(1);
        assertEquals(TileType.BORDER_WALL, level.getTile(25, Level.HEIGHT).type);
    }

    @Test
    void setTile_UpdatesTile() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.FLOOR);
        assertEquals(TileType.FLOOR, level.getTile(10, 10).type);
    }

    @Test
    void setTile_WithVariant() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.FLOOR, 2);
        assertEquals(TileType.FLOOR, level.getTile(10, 10).type);
        assertEquals(2, level.getTile(10, 10).variant);
    }

    @Test
    void setTile_OutOfBounds_NoOp() {
        Level level = new Level(1);
        assertDoesNotThrow(() -> level.setTile(-1, 10, TileType.FLOOR));
        assertDoesNotThrow(() -> level.setTile(10, -1, TileType.FLOOR));
        assertDoesNotThrow(() -> level.setTile(Level.WIDTH, 10, TileType.FLOOR));
    }

    @Test
    void isWalkable_Wall() {
        Level level = new Level(1);
        assertFalse(level.isWalkable(10, 10));
    }

    @Test
    void isWalkable_Floor() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.FLOOR);
        assertTrue(level.isWalkable(10, 10));
    }

    @Test
    void isWalkable_StairsUp() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.STAIRS_UP);
        assertTrue(level.isWalkable(10, 10));
    }

    @Test
    void isWalkable_StairsDown() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.STAIRS_DOWN);
        assertTrue(level.isWalkable(10, 10));
    }

    @Test
    void isWalkable_DoorOpen() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.DOOR_OPEN);
        assertTrue(level.isWalkable(10, 10));
    }

    @Test
    void isWalkable_DoorClosed() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.DOOR_CLOSED);
        assertTrue(level.isWalkable(10, 10));
    }

    @Test
    void isWalkable_BorderWall() {
        Level level = new Level(1);
        assertFalse(level.isWalkable(-1, 25));
    }

    @Test
    void isTransparent_Wall() {
        Level level = new Level(1);
        assertFalse(level.isTransparent(10, 10));
    }

    @Test
    void isTransparent_Floor() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.FLOOR);
        assertTrue(level.isTransparent(10, 10));
    }

    @Test
    void isTransparent_DoorOpen() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.DOOR_OPEN);
        assertTrue(level.isTransparent(10, 10));
    }

    @Test
    void isTransparent_DoorClosed() {
        Level level = new Level(1);
        level.setTile(10, 10, TileType.DOOR_CLOSED);
        assertFalse(level.isTransparent(10, 10));
    }

    @Test
    void isTransparent_OutOfBounds() {
        Level level = new Level(1);
        assertFalse(level.isTransparent(-1, 10));
    }

    @Test
    void isInBounds_Valid() {
        Level level = new Level(1);
        assertTrue(level.isInBounds(0, 0));
        assertTrue(level.isInBounds(Level.WIDTH - 1, Level.HEIGHT - 1));
        assertTrue(level.isInBounds(40, 24));
    }

    @Test
    void isInBounds_NegativeX() {
        Level level = new Level(1);
        assertFalse(level.isInBounds(-1, 25));
    }

    @Test
    void isInBounds_NegativeY() {
        Level level = new Level(1);
        assertFalse(level.isInBounds(25, -1));
    }

    @Test
    void isInBounds_XTooLarge() {
        Level level = new Level(1);
        assertFalse(level.isInBounds(Level.WIDTH, 25));
    }

    @Test
    void isInBounds_YTooLarge() {
        Level level = new Level(1);
        assertFalse(level.isInBounds(25, Level.HEIGHT));
    }

    @Test
    void getTiles_ReturnsArray() {
        Level level = new Level(1);
        var tiles = level.getTiles();
        assertNotNull(tiles);
        assertEquals(Level.WIDTH, tiles.length);
        assertEquals(Level.HEIGHT, tiles[0].length);
    }
}
