package com.tophattowl.dungeonsofvetir.game.world;

public enum TileType {
    WALL(false, false),
    FLOOR(true, true),

    BORDER_WALL(false, false),

    STAIRS_UP(true, true),
    STAIRS_DOWN(true, true),

    DOOR_OPEN(true, true),
    DOOR_CLOSED(true, false);


    public final boolean walkable;
    public final boolean transparent;

    TileType(boolean walkable, boolean transparent) {
        this.walkable = walkable;
        this.transparent = transparent;
    }
}
