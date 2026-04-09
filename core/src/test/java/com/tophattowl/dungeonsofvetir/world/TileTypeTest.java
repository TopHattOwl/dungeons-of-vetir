package com.tophattowl.dungeonsofvetir.world;

import com.tophattowl.dungeonsofvetir.game.world.TileType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TileTypeTest {

    @Test
    void wall_IsNotWalkable() {
        assertFalse(TileType.WALL.walkable);
    }

    @Test
    void wall_IsNotTransparent() {
        assertFalse(TileType.WALL.transparent);
    }

    @Test
    void floor_IsWalkable() {
        assertTrue(TileType.FLOOR.walkable);
    }

    @Test
    void floor_IsTransparent() {
        assertTrue(TileType.FLOOR.transparent);
    }

    @Test
    void borderWall_IsNotWalkable() {
        assertFalse(TileType.BORDER_WALL.walkable);
    }

    @Test
    void borderWall_IsNotTransparent() {
        assertFalse(TileType.BORDER_WALL.transparent);
    }

    @Test
    void stairsUp_IsWalkable() {
        assertTrue(TileType.STAIRS_UP.walkable);
    }

    @Test
    void stairsUp_IsTransparent() {
        assertTrue(TileType.STAIRS_UP.transparent);
    }

    @Test
    void stairsDown_IsWalkable() {
        assertTrue(TileType.STAIRS_DOWN.walkable);
    }

    @Test
    void stairsDown_IsTransparent() {
        assertTrue(TileType.STAIRS_DOWN.transparent);
    }

    @Test
    void doorOpen_IsWalkable() {
        assertTrue(TileType.DOOR_OPEN.walkable);
    }

    @Test
    void doorOpen_IsTransparent() {
        assertTrue(TileType.DOOR_OPEN.transparent);
    }

    @Test
    void doorClosed_IsWalkable() {
        assertTrue(TileType.DOOR_CLOSED.walkable);
    }

    @Test
    void doorClosed_IsNotTransparent() {
        assertFalse(TileType.DOOR_CLOSED.transparent);
    }

    @Test
    void allTileTypes_HaveWalkableAndTransparent() {
        for (TileType type : TileType.values()) {
            assertNotNull(type.walkable, type.name() + " should have walkable field");
            assertNotNull(type.transparent, type.name() + " should have transparent field");
        }
    }
}
