package com.tophattowl.dungeonsofvetir.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void getOpposite_North_ReturnsSouth() {
        assertEquals(Direction.SOUTH, Direction.NORTH.getOpposite());
    }

    @Test
    void getOpposite_South_ReturnsNorth() {
        assertEquals(Direction.NORTH, Direction.SOUTH.getOpposite());
    }

    @Test
    void getOpposite_East_ReturnsWest() {
        assertEquals(Direction.WEST, Direction.EAST.getOpposite());
    }

    @Test
    void getOpposite_West_ReturnsEast() {
        assertEquals(Direction.EAST, Direction.WEST.getOpposite());
    }

    @Test
    void getOpposite_NorthEast_ReturnsSouthWest() {
        assertEquals(Direction.SOUTH_WEST, Direction.NORTH_EAST.getOpposite());
    }

    @Test
    void getOpposite_NorthWest_ReturnsSouthEast() {
        assertEquals(Direction.SOUTH_EAST, Direction.NORTH_WEST.getOpposite());
    }

    @Test
    void getOpposite_SouthEast_ReturnsNorthWest() {
        assertEquals(Direction.NORTH_WEST, Direction.SOUTH_EAST.getOpposite());
    }

    @Test
    void getOpposite_SouthWest_ReturnsNorthEast() {
        assertEquals(Direction.NORTH_EAST, Direction.SOUTH_WEST.getOpposite());
    }

    @Test
    void getOpposite_Stay_ReturnsStay() {
        assertEquals(Direction.STAY, Direction.STAY.getOpposite());
    }

    @Test
    void getOpposite_Idempotent() {
        for (Direction dir : Direction.values()) {
            assertEquals(dir, dir.getOpposite().getOpposite());
        }
    }

    @Test
    void fromDxDy_North() {
        assertEquals(Direction.NORTH, Direction.fromDxDy(0, -1));
    }

    @Test
    void fromDxDy_South() {
        assertEquals(Direction.SOUTH, Direction.fromDxDy(0, 1));
    }

    @Test
    void fromDxDy_East() {
        assertEquals(Direction.EAST, Direction.fromDxDy(1, 0));
    }

    @Test
    void fromDxDy_West() {
        assertEquals(Direction.WEST, Direction.fromDxDy(-1, 0));
    }

    @Test
    void fromDxDy_NorthEast() {
        assertEquals(Direction.NORTH_EAST, Direction.fromDxDy(1, -1));
    }

    @Test
    void fromDxDy_NorthWest() {
        assertEquals(Direction.NORTH_WEST, Direction.fromDxDy(-1, -1));
    }

    @Test
    void fromDxDy_SouthEast() {
        assertEquals(Direction.SOUTH_EAST, Direction.fromDxDy(1, 1));
    }

    @Test
    void fromDxDy_SouthWest() {
        assertEquals(Direction.SOUTH_WEST, Direction.fromDxDy(-1, 1));
    }

    @Test
    void fromDxDy_Stay() {
        assertEquals(Direction.STAY, Direction.fromDxDy(0, 0));
    }

    @Test
    void fromDxDy_Invalid_ReturnsStay() {
        assertEquals(Direction.STAY, Direction.fromDxDy(5, 5));
        assertEquals(Direction.STAY, Direction.fromDxDy(2, -1));
        assertEquals(Direction.STAY, Direction.fromDxDy(-3, 0));
    }

    @Test
    void getDx_ReturnsCorrectValue() {
        assertEquals(0, Direction.NORTH.getDx());
        assertEquals(0, Direction.SOUTH.getDx());
        assertEquals(1, Direction.EAST.getDx());
        assertEquals(-1, Direction.WEST.getDx());
        assertEquals(1, Direction.NORTH_EAST.getDx());
        assertEquals(-1, Direction.NORTH_WEST.getDx());
        assertEquals(1, Direction.SOUTH_EAST.getDx());
        assertEquals(-1, Direction.SOUTH_WEST.getDx());
        assertEquals(0, Direction.STAY.getDx());
    }

    @Test
    void getDy_ReturnsCorrectValue() {
        assertEquals(-1, Direction.NORTH.getDy());
        assertEquals(1, Direction.SOUTH.getDy());
        assertEquals(0, Direction.EAST.getDy());
        assertEquals(0, Direction.WEST.getDy());
        assertEquals(-1, Direction.NORTH_EAST.getDy());
        assertEquals(-1, Direction.NORTH_WEST.getDy());
        assertEquals(1, Direction.SOUTH_EAST.getDy());
        assertEquals(1, Direction.SOUTH_WEST.getDy());
        assertEquals(0, Direction.STAY.getDy());
    }

    @Test
    void allDirectionsHaveUniqueDxDy() {
        var seen = new java.util.HashSet<String>();
        for (Direction dir : Direction.values()) {
            String key = dir.getDx() + "," + dir.getDy();
            assertTrue(seen.add(key), "Duplicate (dx,dy) found for " + dir);
        }
    }
}
