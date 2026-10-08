package com.tophattowl.dungeonsofvetir.game.generation.authored;

import com.tophattowl.dungeonsofvetir.game.world.DeclaredExit;
import com.tophattowl.dungeonsofvetir.game.world.Level;

import java.util.List;

/**
 * A premade zone loaded from a text map: the tile grid plus any declared exits
 */
public record AuthoredMap(String name, Level level, List<DeclaredExit> exits) {
    public AuthoredMap {
        exits = List.copyOf(exits);
    }
}
