package com.tophattowl.dungeonsofvetir.dungeon;

import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.rng.SeedConfig;
import com.tophattowl.dungeonsofvetir.game.world.Exit;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.PlaceKind;
import com.tophattowl.dungeonsofvetir.game.world.Point;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import com.tophattowl.dungeonsofvetir.game.world.TransitionKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LevelTransitionTest {

    private static Exit exitOfKind(GameWorld world, TransitionKind kind) {
        return world.getExits().stream()
            .filter(e -> e.kind() == kind)
            .findFirst()
            .orElse(null);
    }

    @Test
    void startsOnTheDungeonSurface() {
        GameWorld world = new GameWorld(SeedConfig.custom(1));
        assertEquals(PlaceKind.DUNGEON_SURFACE, world.getCurrentPlace().kind());
    }

    @Test
    void surfaceHasDescendExitButNoAscendExit() {
        GameWorld world = new GameWorld(SeedConfig.custom(1));

        Exit down = exitOfKind(world, TransitionKind.STAIRS_DOWN);
        assertNotNull(down);
        assertEquals(PlaceKind.DUNGEON_FLOOR, down.target().kind());
        assertEquals(1, down.target().depth());
        assertNull(exitOfKind(world, TransitionKind.STAIRS_UP), "surface has nothing above it yet");
    }

    @Test
    void firstFloorHasBothExits() {
        GameWorld world = new GameWorld(SeedConfig.custom(1));
        world.enterLevel(1);

        assertEquals(2, exitOfKind(world, TransitionKind.STAIRS_DOWN).target().depth());
        assertEquals(PlaceKind.DUNGEON_SURFACE,
            exitOfKind(world, TransitionKind.STAIRS_UP).target().kind());
    }

    @Test
    void descendingLandsOnTheUpperStairsOfTheNextFloor() {
        GameWorld world = new GameWorld(SeedConfig.custom(1));
        world.enterLevel(1);

        Exit down = exitOfKind(world, TransitionKind.STAIRS_DOWN);
        world.getPlayer().getComponent(PositionComponent.class).set(down.tile());
        world.useExit(down);

        assertEquals(2, world.getCurrentFloor());
        PositionComponent pos = world.getPlayer().getComponent(PositionComponent.class);
        assertEquals(TileType.STAIRS_UP, world.getCurrentLevel().getTile(pos.getX(), pos.getY()).type);
    }

    @Test
    void ascendingFromFirstFloorReturnsToTheSurface() {
        GameWorld world = new GameWorld(SeedConfig.custom(1));
        world.enterLevel(1);

        Exit up = exitOfKind(world, TransitionKind.STAIRS_UP);
        world.getPlayer().getComponent(PositionComponent.class).set(up.tile());
        world.useExit(up);

        assertEquals(PlaceKind.DUNGEON_SURFACE, world.getCurrentPlace().kind());
        PositionComponent pos = world.getPlayer().getComponent(PositionComponent.class);
        assertEquals(TileType.STAIRS_DOWN, world.getCurrentLevel().getTile(pos.getX(), pos.getY()).type);
    }

    @Test
    void lastFloorHasAscendExitButNoDescendExit() {
        GameWorld world = new GameWorld(SeedConfig.custom(1));
        world.enterLevel(11);

        assertNotNull(exitOfKind(world, TransitionKind.STAIRS_UP));
        assertNull(exitOfKind(world, TransitionKind.STAIRS_DOWN), "no floor below the last");
    }

    @Test
    void noExitAtAnEmptyTile() {
        GameWorld world = new GameWorld(SeedConfig.custom(1));
        assertNull(world.exitAt(new Point(-1, -1)));
    }
}
