package com.tophattowl.dungeonsofvetir.game.world;

import java.util.ArrayList;
import java.util.List;

public class Level {
    public static final int DEFAULT_WIDTH = 82;
    public static final int DEFAULT_HEIGHT = 49;

    private final Tile[][] tiles;
    public final int floorNumber;
    private final int width;
    private final int height;
    private List<DeclaredExit> declaredExits = new ArrayList<>();

    public Level(int floorNumber) {
        this(floorNumber, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Level(int floorNumber, int width, int height) {
        this.floorNumber = floorNumber;
        this.width = width;
        this.height = height;

        this.tiles = new Tile[width][height];

        // fill with walls
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(TileType.WALL);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return new Tile(TileType.BORDER_WALL);
        }
        return tiles[x][y];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || y < 0 || x >= width || y >= height) return;
        tiles[x][y] = tile;
    }

    public void setTile(int x, int y, TileType type) {
        setTile(x, y, new Tile(type));
    }

    public void setTile(int x, int y, TileType type, int variant) {
        setTile(x, y, new Tile(type, variant));
    }

    public boolean isWalkable(int x, int y) {
        return getTile(x, y).isWalkable();
    }

    public boolean isTransparent(int x, int y) {
        return getTile(x, y).isTransparent();
    }

    public boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public List<DeclaredExit> getDeclaredExits() {
        return declaredExits;
    }

    public void setDeclaredExits(List<DeclaredExit> declaredExits) {
        this.declaredExits = declaredExits;
    }
}
