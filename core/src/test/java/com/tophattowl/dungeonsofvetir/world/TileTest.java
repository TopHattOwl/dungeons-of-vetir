package com.tophattowl.dungeonsofvetir.world;

import com.tophattowl.dungeonsofvetir.game.world.Tile;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void constructor_SetsType() {
        Tile tile = new Tile(TileType.FLOOR);
        assertEquals(TileType.FLOOR, tile.type);
    }

    @Test
    void constructor_SetsVariant() {
        Tile tile = new Tile(TileType.FLOOR, 3);
        assertEquals(TileType.FLOOR, tile.type);
        assertEquals(3, tile.variant);
    }

    @Test
    void constructor_DefaultVariant() {
        Tile tile = new Tile(TileType.WALL);
        assertEquals(0, tile.variant);
    }

    @Test
    void isWalkable_ReturnsTypeValue() {
        Tile floor = new Tile(TileType.FLOOR);
        Tile wall = new Tile(TileType.WALL);
        Tile stairsUp = new Tile(TileType.STAIRS_UP);
        Tile doorClosed = new Tile(TileType.DOOR_CLOSED);

        assertTrue(floor.isWalkable());
        assertFalse(wall.isWalkable());
        assertTrue(stairsUp.isWalkable());
        assertTrue(doorClosed.isWalkable());
    }

    @Test
    void isTransparent_ReturnsTypeValue() {
        Tile floor = new Tile(TileType.FLOOR);
        Tile wall = new Tile(TileType.WALL);
        Tile doorOpen = new Tile(TileType.DOOR_OPEN);
        Tile doorClosed = new Tile(TileType.DOOR_CLOSED);

        assertTrue(floor.isTransparent());
        assertFalse(wall.isTransparent());
        assertTrue(doorOpen.isTransparent());
        assertFalse(doorClosed.isTransparent());
    }

    @Test
    void sameType_AreNotSameTile() {
        Tile tile1 = new Tile(TileType.FLOOR, 1);
        Tile tile2 = new Tile(TileType.FLOOR, 2);
        assertNotEquals(tile1, tile2);
    }

    @Test
    void differentVariants_AreNotEqual() {
        Tile tile1 = new Tile(TileType.FLOOR, 0);
        Tile tile2 = new Tile(TileType.FLOOR, 1);
        assertNotEquals(tile1, tile2);
    }

    @Test
    void typeChange_AffectsWalkableAndTransparent() {
        Tile tile = new Tile(TileType.FLOOR);
        assertTrue(tile.isWalkable());
        assertTrue(tile.isTransparent());

        tile.type = TileType.WALL;
        assertFalse(tile.isWalkable());
        assertFalse(tile.isTransparent());
    }
}
