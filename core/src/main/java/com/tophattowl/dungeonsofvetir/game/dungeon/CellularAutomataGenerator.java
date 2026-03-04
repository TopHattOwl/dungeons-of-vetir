package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

import java.util.*;

/**
 * Generates cave-like levels using cellular automata.
 * Algorithm:
 * 1. Fill grid randomly (wall/floor based on fillChance)
 * 2. Run several "smoothing" passes — a cell becomes wall if it has >= wallThreshold neighbours
 * 3. Flood-fill to find the largest connected open region
 * 4. Discard all open cells not in that region (so the cave is one connected space)
 * 5. Place stairs
 */
public class CellularAutomataGenerator {

    // Tweak these to get different cave feels
    private static final double FILL_CHANCE   = 0.48; // probability a cell starts as wall
    private static final int    SMOOTH_PASSES = 5;    // how many CA iterations
    private static final int    WALL_THRESHOLD = 5;   // neighbours needed to become/stay wall

    // Floor variants — how many visual variants floor tiles have (0-based index)
    private static final int    FLOOR_VARIANTS = 4;

    private final Random rng;

    public CellularAutomataGenerator(long seed) {
        this.rng = new Random(seed);
    }

    public CellularAutomataGenerator() {
        this.rng = new Random();
    }

    public Level generate(int floorNumber) {
        Level level = new Level(floorNumber);
        boolean[][] grid = new boolean[Level.WIDTH][Level.HEIGHT]; // true = wall

        // --- Step 1: Random fill ---
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                // Always wall on borders
                if (x == 0 || y == 0 || x == Level.WIDTH - 1 || y == Level.HEIGHT - 1) {
                    grid[x][y] = true;
                } else {
                    grid[x][y] = rng.nextDouble() < FILL_CHANCE;
                }
            }
        }

        // --- Step 2: Smooth passes ---
        for (int pass = 0; pass < SMOOTH_PASSES; pass++) {
            grid = smooth(grid);
        }

        // --- Step 3: Find largest connected open region ---
        boolean[][] inMainRegion = largestRegion(grid);

        // --- Step 4: Write tiles to level ---
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                if (!grid[x][y] && inMainRegion[x][y]) {
                    int variant = rng.nextInt(FLOOR_VARIANTS);
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

    // -------------------------------------------------------------------------

    private boolean[][] smooth(boolean[][] grid) {
        boolean[][] next = new boolean[Level.WIDTH][Level.HEIGHT];
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                if (x == 0 || y == 0 || x == Level.WIDTH - 1 || y == Level.HEIGHT - 1) {
                    next[x][y] = true; // border always wall
                    continue;
                }
                int walls = countWallNeighbours(grid, x, y);
                // Classic CA rule: become wall if enough wall neighbours
                next[x][y] = walls >= WALL_THRESHOLD;
            }
        }
        return next;
    }

    private int countWallNeighbours(boolean[][] grid, int cx, int cy) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = cx + dx;
                int ny = cy + dy;
                if (nx < 0 || ny < 0 || nx >= Level.WIDTH || ny >= Level.HEIGHT) {
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
    private boolean[][] largestRegion(boolean[][] grid) {
        boolean[][] visited = new boolean[Level.WIDTH][Level.HEIGHT];
        boolean[][] bestRegion = new boolean[Level.WIDTH][Level.HEIGHT];
        int bestSize = 0;

        for (int startX = 0; startX < Level.WIDTH; startX++) {
            for (int startY = 0; startY < Level.HEIGHT; startY++) {
                if (grid[startX][startY] || visited[startX][startY]) continue;

                // BFS
                boolean[][] region = new boolean[Level.WIDTH][Level.HEIGHT];
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
                        if (nx < 0 || ny < 0 || nx >= Level.WIDTH || ny >= Level.HEIGHT) continue;
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
        // Find open floor tiles for stairs placement
        List<int[]> floorTiles = new ArrayList<>();
        for (int x = 1; x < Level.WIDTH - 1; x++)
            for (int y = 1; y < Level.HEIGHT - 1; y++)
                if (level.getTile(x, y).type == TileType.FLOOR)
                    floorTiles.add(new int[]{x, y});

        if (floorTiles.size() < 2) return; // shouldn't happen with a well-generated cave

        // Place stairs up near one end, stairs down near another
        // Simple approach: pick two tiles that are far from each other
        int[] upPos   = floorTiles.get(0);
        int[] downPos = floorTiles.get(floorTiles.size() - 1);

        level.setTile(upPos[0],   upPos[1],   TileType.STAIRS_UP);
        level.setTile(downPos[0], downPos[1], TileType.STAIRS_DOWN);
    }

    private static final int[][] DIRS = {{0,1},{0,-1},{1,0},{-1,0}};
}
