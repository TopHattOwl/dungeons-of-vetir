package com.tophattowl.dungeonsofvetir.game.world;

public class Level {
    public static final int WIDTH = 82;
    public static final int HEIGHT = 49;

    private Tile[][] tiles;
    public final int floorNumber;

    public Level(int floorNumber) {
        this.floorNumber = floorNumber;

        this.tiles = new Tile[WIDTH][HEIGHT];

        // fill with walls
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(TileType.WALL);
            }
        }
    }


    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return new Tile(TileType.BORDER_WALL);
        }
        return tiles[x][y];
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setTile(int x, int y, Tile tile) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) return;
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
        return x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT;
    }
}
