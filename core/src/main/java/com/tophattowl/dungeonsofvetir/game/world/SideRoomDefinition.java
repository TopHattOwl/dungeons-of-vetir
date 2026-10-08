package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;

/**
 * A nested side room reachable from a parent zone (a cellar, a vault, a ship hold).
 * Its generator may be AUTHORED (premade) or a procedural one (e.g. CAVE).
 */
public record SideRoomDefinition(String key, String name, GeneratorId generator, TileTheme theme) {
}
