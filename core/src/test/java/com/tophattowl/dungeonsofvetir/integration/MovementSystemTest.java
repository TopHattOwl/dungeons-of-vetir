package com.tophattowl.dungeonsofvetir.integration;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import com.tophattowl.dungeonsofvetir.util.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MovementSystemTest {

    @BeforeEach
    void setUp() {
        FactionRelation.init();
    }

    private Level createLevel() {
        Level level = new Level(1);
        for (int x = 1; x < Level.WIDTH - 1; x++) {
            for (int y = 1; y < Level.HEIGHT - 1; y++) {
                level.setTile(x, y, TileType.FLOOR);
            }
        }
        return level;
    }

    private Entity createEntityAt(int x, int y) {
        Entity entity = new Entity();
        entity.addComponent(new PositionComponent(x, y));
        entity.addComponent(new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER));
        return entity;
    }

    @Test
    void level_WalkableFloor() {
        Level level = createLevel();
        assertTrue(level.isWalkable(10, 10));
    }

    @Test
    void level_WallNotWalkable() {
        Level level = new Level(1);
        assertFalse(level.isWalkable(10, 10));
    }

    @Test
    void positionComponent_SetPosition() {
        PositionComponent pos = new PositionComponent(5, 10);
        assertEquals(5, pos.getX());
        assertEquals(10, pos.getY());
        assertEquals(new Point(5, 10), pos.getPosition());

        pos.set(15, 20);
        assertEquals(15, pos.getX());
        assertEquals(20, pos.getY());
    }

    @Test
    void positionComponent_SetXAndY() {
        PositionComponent pos = new PositionComponent(0, 0);
        pos.setX(25);
        pos.setY(30);
        assertEquals(25, pos.getX());
        assertEquals(30, pos.getY());
    }

    @Test
    void direction_NorthDelta() {
        assertEquals(0, Direction.NORTH.getDx());
        assertEquals(-1, Direction.NORTH.getDy());
    }

    @Test
    void direction_SouthDelta() {
        assertEquals(0, Direction.SOUTH.getDx());
        assertEquals(1, Direction.SOUTH.getDy());
    }

    @Test
    void direction_EastDelta() {
        assertEquals(1, Direction.EAST.getDx());
        assertEquals(0, Direction.EAST.getDy());
    }

    @Test
    void direction_WestDelta() {
        assertEquals(-1, Direction.WEST.getDx());
        assertEquals(0, Direction.WEST.getDy());
    }

    @Test
    void direction_DiagonalDeltas() {
        assertEquals(1, Direction.NORTH_EAST.getDx());
        assertEquals(-1, Direction.NORTH_EAST.getDy());
        assertEquals(-1, Direction.NORTH_WEST.getDx());
        assertEquals(-1, Direction.NORTH_WEST.getDy());
        assertEquals(1, Direction.SOUTH_EAST.getDx());
        assertEquals(1, Direction.SOUTH_EAST.getDy());
        assertEquals(-1, Direction.SOUTH_WEST.getDx());
        assertEquals(1, Direction.SOUTH_WEST.getDy());
    }

    @Test
    void direction_StayDelta() {
        assertEquals(0, Direction.STAY.getDx());
        assertEquals(0, Direction.STAY.getDy());
    }

    @Test
    void calculateNewPosition_North() {
        int x = 10;
        int y = 10;
        int newX = x + Direction.NORTH.getDx();
        int newY = y + Direction.NORTH.getDy();
        assertEquals(10, newX);
        assertEquals(9, newY);
    }

    @Test
    void calculateNewPosition_South() {
        int x = 10;
        int y = 10;
        int newX = x + Direction.SOUTH.getDx();
        int newY = y + Direction.SOUTH.getDy();
        assertEquals(10, newX);
        assertEquals(11, newY);
    }

    @Test
    void calculateNewPosition_East() {
        int x = 10;
        int y = 10;
        int newX = x + Direction.EAST.getDx();
        int newY = y + Direction.EAST.getDy();
        assertEquals(11, newX);
        assertEquals(10, newY);
    }

    @Test
    void calculateNewPosition_West() {
        int x = 10;
        int y = 10;
        int newX = x + Direction.WEST.getDx();
        int newY = y + Direction.WEST.getDy();
        assertEquals(9, newX);
        assertEquals(10, newY);
    }

    @Test
    void calculateNewPosition_DiagonalNE() {
        int x = 10;
        int y = 10;
        int newX = x + Direction.NORTH_EAST.getDx();
        int newY = y + Direction.NORTH_EAST.getDy();
        assertEquals(11, newX);
        assertEquals(9, newY);
    }

    @Test
    void calculateNewPosition_Stay() {
        int x = 10;
        int y = 10;
        int newX = x + Direction.STAY.getDx();
        int newY = y + Direction.STAY.getDy();
        assertEquals(10, newX);
        assertEquals(10, newY);
    }

    @Test
    void level_InBounds_Checks() {
        Level level = createLevel();
        assertTrue(level.isInBounds(0, 0));
        assertTrue(level.isInBounds(Level.WIDTH - 1, Level.HEIGHT - 1));
        assertTrue(level.isInBounds(40, 24));
    }

    @Test
    void level_InBounds_Negative() {
        Level level = createLevel();
        assertFalse(level.isInBounds(-1, 0));
        assertFalse(level.isInBounds(0, -1));
    }

    @Test
    void level_InBounds_TooLarge() {
        Level level = createLevel();
        assertFalse(level.isInBounds(Level.WIDTH, 0));
        assertFalse(level.isInBounds(0, Level.HEIGHT));
    }

    @Test
    void entity_CanStoreComponents() {
        Entity entity = new Entity();
        PositionComponent pos = new PositionComponent(10, 10);
        IdentityComponent identity = new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER);

        entity.addComponent(pos);
        entity.addComponent(identity);

        assertNotNull(entity.getComponent(PositionComponent.class));
        assertNotNull(entity.getComponent(IdentityComponent.class));
        assertEquals(pos, entity.getComponent(PositionComponent.class));
    }
}
