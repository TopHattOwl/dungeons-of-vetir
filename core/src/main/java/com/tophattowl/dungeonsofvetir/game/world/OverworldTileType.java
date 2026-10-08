package com.tophattowl.dungeonsofvetir.game.world;

/**
 * What an overworld tile leads to. Every tile is enterable; content is defined by
 * biome/settlement/dungeon definitions referenced via {@code contentKey}.
 */
public enum OverworldTileType {
    WILDERNESS,
    SETTLEMENT,
    DUNGEON,
}
