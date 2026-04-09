package com.tophattowl.dungeonsofvetir.world;

import com.tophattowl.dungeonsofvetir.game.world.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void constructor_SetsCoordinates() {
        Point point = new Point(5, 10);
        assertEquals(5, point.x);
        assertEquals(10, point.y);
    }

    @Test
    void equals_Reflexive() {
        Point point = new Point(3, 4);
        assertTrue(point.equals(point));
    }

    @Test
    void equals_Symmetric() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
    }

    @Test
    void equals_SameCoordinates() {
        Point p1 = new Point(5, 10);
        Point p2 = new Point(5, 10);
        assertTrue(p1.equals(p2));
    }

    @Test
    void equals_DifferentCoordinates() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 1);
        assertFalse(p1.equals(p2));
    }

    @Test
    void equals_DifferentX() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(3, 2);
        assertFalse(p1.equals(p2));
    }

    @Test
    void equals_DifferentY() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 3);
        assertFalse(p1.equals(p2));
    }

    @Test
    void equals_Null() {
        Point point = new Point(1, 2);
        assertFalse(point.equals(null));
    }

    @Test
    void equals_DifferentClass() {
        Point point = new Point(1, 2);
        assertFalse(point.equals("string"));
        assertFalse(point.equals(12));
    }

    @Test
    void hashCode_SamePoint_SameHash() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void hashCode_DifferentPoints_MayDiffer() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(2, 1);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void hashCode_Consistency() {
        Point point = new Point(3, 4);
        int hash1 = point.hashCode();
        int hash2 = point.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void setX_UpdatesValue() {
        Point point = new Point(1, 2);
        point.setX(5);
        assertEquals(5, point.x);
        assertEquals(2, point.y);
    }

    @Test
    void setY_UpdatesValue() {
        Point point = new Point(1, 2);
        point.setY(10);
        assertEquals(1, point.x);
        assertEquals(10, point.y);
    }

    @Test
    void toString_ContainsCoordinates() {
        Point point = new Point(3, 4);
        String str = point.toString();
        assertTrue(str.contains("3"));
        assertTrue(str.contains("4"));
    }

    @Test
    void zeroCoordinates() {
        Point point = new Point(0, 0);
        assertEquals(0, point.x);
        assertEquals(0, point.y);
        assertTrue(point.equals(new Point(0, 0)));
    }

    @Test
    void negativeCoordinates() {
        Point point = new Point(-5, -10);
        assertEquals(-5, point.x);
        assertEquals(-10, point.y);
        assertTrue(point.equals(new Point(-5, -10)));
    }
}
