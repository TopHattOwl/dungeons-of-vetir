package com.tophattowl.dungeonsofvetir.game.world;

/**
 * One tile of the fixed overworld grid. {@code contentKey} references a biome,
 * settlement or dungeon definition (empty for generic wilderness)
 */
public record OverworldTile(int x, int y, OverworldTileType type, String contentKey) {
}
