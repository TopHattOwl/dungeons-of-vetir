package com.tophattowl.dungeonsofvetir.game.generation.generators;

import com.tophattowl.dungeonsofvetir.game.generation.GenerationContext;
import com.tophattowl.dungeonsofvetir.game.generation.LevelGenerator;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Generates cave-like levels using cellular automata.
 * Algorithm:
 * 1. Fill grid randomly (wall/floor based on fillChance)
 * 2. Run several "smoothing" passes - a cell becomes wall if it has >= wallThreshold neighbours
 * 3. Flood-fill to find the largest connected open region
 * 4. Discard all open cells not in that region (so the cave is one connected space)
 * 5. Place stairs
 */
public class CaveGenerator implements LevelGenerator {

    private final CaveParams params;

    public CaveGenerator() {
        this(CaveParams.DEFAULT);
    }

    public CaveGenerator(CaveParams params) {
        this.params = params;
    }

    @Override
    public Level generate(GenerationContext ctx) {
        Level level = new Level(ctx.floorNumber(), ctx.width(), ctx.height());
        int width = level.getWidth();
        int height = level.getHeight();
        Random rng = new Random(ctx.seed());
        boolean[][] grid = new boolean[width][height]; // true = wall

        // --- Step 1: Random fill ---
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    grid[x][y] = true;
                } else {
                    grid[x][y] = rng.nextDouble() < params.fillChance();
                }
            }
        }

        // --- Step 2: Smooth passes ---
        for (int pass = 0; pass < params.smoothPasses(); pass++) {
            grid = smooth(grid, width, height);
        }

        // --- Step 3: Find largest connected open region ---
        boolean[][] inMainRegion = largestRegion(grid, width, height);

        // --- Step 4: Write tiles to level ---
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (!grid[x][y] && inMainRegion[x][y]) {
                    int variant = rng.nextInt(params.floorVariants());
                    level.setTile(x, y, TileType.FLOOR, variant);
                } else {
                    level.setTile(x, y, TileType.WALL, 0);
                }
            }
        }

        // --- Step 5: Place stairs ---
        placeStairs(level);

        return level;
    }

    private boolean[][] smooth(boolean[][] grid, int width, int height) {
        boolean[][] next = new boolean[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    next[x][y] = true; // border always wall
                    continue;
                }
                int walls = countWallNeighbours(grid, x, y, width, height);
                next[x][y] = walls >= params.wallThreshold();
            }
        }
        return next;
    }

    private int countWallNeighbours(boolean[][] grid, int cx, int cy, int width, int height) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = cx + dx;
                int ny = cy + dy;
                if (nx < 0 || ny < 0 || nx >= width || ny >= height) {
                    count++; // out-of-bounds counts as wall
                } else if (grid[nx][ny]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Flood fill from every open cell to find connected regions.
     * Returns a boolean grid marking only the largest region.
     */
    private boolean[][] largestRegion(boolean[][] grid, int width, int height) {
        boolean[][] visited = new boolean[width][height];
        boolean[][] bestRegion = new boolean[width][height];
        int bestSize = 0;

        for (int startX = 0; startX < width; startX++) {
            for (int startY = 0; startY < height; startY++) {
                if (grid[startX][startY] || visited[startX][startY]) continue;

                boolean[][] region = new boolean[width][height];
                Queue<int[]> queue = new LinkedList<>();
                queue.add(new int[]{startX, startY});
                visited[startX][startY] = true;
                int size = 0;

                while (!queue.isEmpty()) {
                    int[] cell = queue.poll();
                    int x = cell[0], y = cell[1];
                    region[x][y] = true;
                    size++;

                    for (int[] dir : DIRS) {
                        int nx = x + dir[0];
                        int ny = y + dir[1];
                        if (nx < 0 || ny < 0 || nx >= width || ny >= height) continue;
                        if (visited[nx][ny] || grid[nx][ny]) continue;
                        visited[nx][ny] = true;
                        queue.add(new int[]{nx, ny});
                    }
                }

                if (size > bestSize) {
                    bestSize = size;
                    bestRegion = region;
                }
            }
        }
        return bestRegion;
    }

    private void placeStairs(Level level) {
        List<int[]> floorTiles = new ArrayList<>();
        for (int x = 1; x < level.getWidth() - 1; x++)
            for (int y = 1; y < level.getHeight() - 1; y++)
                if (level.getTile(x, y).type == TileType.FLOOR)
                    floorTiles.add(new int[]{x, y});

        if (floorTiles.size() < 2) return; // shouldn't happen with a well-generated cave

        // Simple: pick two tiles far apart in scan order.
        // TODO: something more random
        int[] upPos = floorTiles.get(0);
        int[] downPos = floorTiles.get(floorTiles.size() - 1);

        level.setTile(upPos[0], upPos[1], TileType.STAIRS_UP);
        level.setTile(downPos[0], downPos[1], TileType.STAIRS_DOWN);
    }

    private static final int[][] DIRS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
}
