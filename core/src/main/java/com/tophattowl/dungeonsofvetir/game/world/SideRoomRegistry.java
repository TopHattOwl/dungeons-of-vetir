package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;

import java.util.HashMap;
import java.util.Map;

/**
 * Content table of side rooms. Add an entry to make a door/hatch lead somewhere.
 */
public final class SideRoomRegistry {

    private static final Map<String, SideRoomDefinition> sideRooms = new HashMap<>();

    static {
        register(new SideRoomDefinition("cellar", "Cellar", GeneratorId.AUTHORED, TileTheme.CAVES_DANK));
    }

    private SideRoomRegistry() {}

    public static void register(SideRoomDefinition definition) {
        sideRooms.put(definition.key(), definition);
    }

    public static SideRoomDefinition get(String key) {
        return sideRooms.get(key);
    }
}
