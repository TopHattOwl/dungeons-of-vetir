package com.tophattowl.dungeonsofvetir.game.world;

/**
 * The fixed overworld grid. The layout is identical every run; each tile resolves
 * to a zone (biome/settlement/dungeon) when entered. Content is authored later.
 */
public class WorldMap {

    private final int width;
    private final int height;
    private final OverworldTile[][] tiles;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new OverworldTile[width][height];
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public OverworldTile tileAt(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) return null;
        return tiles[x][y];
    }

    public void setTile(OverworldTile tile) {
        if (tile.x() < 0 || tile.y() < 0 || tile.x() >= width || tile.y() >= height) return;
        tiles[tile.x()][tile.y()] = tile;
    }
}
