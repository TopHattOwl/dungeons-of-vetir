package com.tophattowl.dungeonsofvetir.actors.components;

import com.tophattowl.dungeonsofvetir.game.actors.components.FovComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FovComponentTest {

    @Test
    void constructor_SetsRadius() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertEquals(10, fov.visionRadius);
    }

    @Test
    void constructor_InitializesArrays() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertNotNull(fov.visibleTiles);
        assertNotNull(fov.exploredTiles);
        assertEquals(82, fov.visibleTiles.length);
        assertEquals(49, fov.visibleTiles[0].length);
    }

    @Test
    void constructor_InitializesArraysToFalse() {
        FovComponent fov = new FovComponent(10, 82, 49);
        for (boolean[] col : fov.visibleTiles) {
            for (boolean val : col) {
                assertFalse(val);
            }
        }
        for (boolean[] col : fov.exploredTiles) {
            for (boolean val : col) {
                assertFalse(val);
            }
        }
    }

    @Test
    void isVisible_OutOfBounds_NegativeX() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertFalse(fov.isVisible(-1, 25));
    }

    @Test
    void isVisible_OutOfBounds_NegativeY() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertFalse(fov.isVisible(25, -1));
    }

    @Test
    void isVisible_OutOfBounds_XTooLarge() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertFalse(fov.isVisible(82, 25));
    }

    @Test
    void isVisible_OutOfBounds_YTooLarge() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertFalse(fov.isVisible(25, 49));
    }

    @Test
    void isVisible_InBounds_DefaultFalse() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertFalse(fov.isVisible(40, 24));
    }

    @Test
    void isVisible_InBounds_AfterSet() {
        FovComponent fov = new FovComponent(10, 82, 49);
        fov.visibleTiles[40][24] = true;
        assertTrue(fov.isVisible(40, 24));
    }

    @Test
    void isExplored_OutOfBounds_NegativeX() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertFalse(fov.isExplored(-1, 25));
    }

    @Test
    void isExplored_OutOfBounds_YTooLarge() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertFalse(fov.isExplored(25, 49));
    }

    @Test
    void isExplored_InBounds_DefaultFalse() {
        FovComponent fov = new FovComponent(10, 82, 49);
        assertFalse(fov.isExplored(40, 24));
    }

    @Test
    void isExplored_InBounds_AfterSet() {
        FovComponent fov = new FovComponent(10, 82, 49);
        fov.exploredTiles[40][24] = true;
        assertTrue(fov.isExplored(40, 24));
    }

    @Test
    void clearVisible_SetsAllToFalse() {
        FovComponent fov = new FovComponent(10, 82, 49);
        fov.visibleTiles[10][10] = true;
        fov.visibleTiles[20][20] = true;
        fov.visibleTiles[30][30] = true;

        fov.clearVisible();

        for (boolean[] col : fov.visibleTiles) {
            for (boolean val : col) {
                assertFalse(val);
            }
        }
    }

    @Test
    void clearVisible_PreservesExplored() {
        FovComponent fov = new FovComponent(10, 82, 49);
        fov.exploredTiles[10][10] = true;
        fov.exploredTiles[20][20] = true;

        fov.clearVisible();

        assertTrue(fov.isExplored(10, 10));
        assertTrue(fov.isExplored(20, 20));
    }

    @Test
    void smallLevel() {
        FovComponent fov = new FovComponent(5, 10, 10);
        assertEquals(5, fov.visionRadius);
        assertEquals(10, fov.visibleTiles.length);
        assertEquals(10, fov.visibleTiles[0].length);
    }
}
