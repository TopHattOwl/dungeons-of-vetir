package com.tophattowl.dungeonsofvetir.game.world;

/**
 * The kind of zone a {@link Place} refers to. Only {@link #DUNGEON_SURFACE} and
 * {@link #DUNGEON_FLOOR} are generated for now; the rest are seams for the overworld
 */
public enum PlaceKind {
    OVERWORLD,
    SETTLEMENT,
    BIOME,
    DUNGEON_SURFACE,
    DUNGEON_FLOOR,
    SIDE_ROOM,
}
