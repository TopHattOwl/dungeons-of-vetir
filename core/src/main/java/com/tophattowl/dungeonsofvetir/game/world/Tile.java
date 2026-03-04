package com.tophattowl.dungeonsofvetir.game.world;

public class Tile {
    public TileType type;
    public final int variant;

    public Tile(TileType type, int variant) {
        this.type = type;
        this.variant = variant;
    }

    public Tile(TileType type) {
        this(type, 0);
    }

    public boolean isWalkable() {
        return type.walkable;
    }

    public boolean isTransparent() {
        return type.transparent;
    }
}
