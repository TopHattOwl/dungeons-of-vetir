package com.tophattowl.dungeonsofvetir.dungeon;

import com.tophattowl.dungeonsofvetir.game.generation.authored.AuthoredMap;
import com.tophattowl.dungeonsofvetir.game.generation.authored.AuthoredMapLoader;
import com.tophattowl.dungeonsofvetir.game.world.PlaceKind;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import com.tophattowl.dungeonsofvetir.game.world.TransitionKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthoredMapLoaderTest {

    private static final String MAP = """
        name: Cellar
        width: 6
        height: 5
        tiles:
        ######
        #....#
        #.+..#
        #....#
        ######
        exits:
        2 2 DOOR SIDE_ROOM cellar_back
        """;

    @Test
    void parsesDimensionsAndTiles() {
        AuthoredMap map = AuthoredMapLoader.parse(MAP, 0);

        assertEquals("Cellar", map.name());
        assertEquals(6, map.level().getWidth());
        assertEquals(5, map.level().getHeight());
        assertEquals(TileType.WALL, map.level().getTile(0, 0).type);
        assertEquals(TileType.FLOOR, map.level().getTile(1, 1).type);
        assertEquals(TileType.DOOR_OPEN, map.level().getTile(2, 2).type);
    }

    @Test
    void parsesDeclaredExits() {
        AuthoredMap map = AuthoredMapLoader.parse(MAP, 0);

        assertEquals(1, map.exits().size());
        assertEquals(2, map.exits().get(0).tile().x);
        assertEquals(2, map.exits().get(0).tile().y);
        assertEquals(TransitionKind.DOOR, map.exits().get(0).kind());
        assertEquals(PlaceKind.SIDE_ROOM, map.exits().get(0).targetKind());
        assertEquals("cellar_back", map.exits().get(0).targetKey());
        assertEquals(1, map.level().getDeclaredExits().size());
    }

    @Test
    void usesRequestedFloorNumber() {
        AuthoredMap map = AuthoredMapLoader.parse(MAP, 4);
        assertEquals(4, map.level().floorNumber);
    }
}
