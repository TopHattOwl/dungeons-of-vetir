package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;

/**
 * A premade settlement: which generator builds its level and how it looks.
 * Content is registered later.
 */
public record SettlementDefinition(String key, String name, GeneratorId generator, TileTheme theme) {
}
