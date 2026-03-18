package com.tophattowl.dungeonsofvetir.util.dijkstra;


import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.Arrays;

public abstract class DijkstraMap {
    protected static final int BASE_VALUE = 100;
    protected static final int OBSTACLE_VALUE = Integer.MAX_VALUE;
    protected static final int GOAL_VALUE = 0;

    protected int[][] map;

    public DijkstraMap(int width, int height) {
        map = new int[width][height];
    }

    public void initialize(GameWorld gameWorld) {}

    public void calculate() {
        boolean hasChanged = false;
        do {
            hasChanged = false;
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[x].length; y++) {
                    if (map[x][y] == OBSTACLE_VALUE) continue;

                    if (checkNeighbors(x, y)) hasChanged = true;
                }
            }
        } while (hasChanged);
    }

    protected boolean checkNeighbors(int x, int y) {
        int lowestValue = map[x][y];
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                // skip self, walls and out of bounds
                if (x + dx < 0 || y + dy < 0 || x + dx >= map.length || y + dy >= map[x].length) continue;
                if (dx == 0 && dy == 0) continue;
                if (map[x+dx][y+dy] == OBSTACLE_VALUE) continue;

                if (map[x+dx][y+dy] < lowestValue) {
                    lowestValue = map[x+dx][y+dy];
                }
            }
        }

        if (map[x][y] > lowestValue + 1) {
            map[x][y] = lowestValue + 1;
            return true;
        }

        return false;
    }

    public void logDijkstraMap() {
        StringBuilder sb = new StringBuilder();

        for (int[] row : map) {
            sb.append(Arrays.toString(row)).append("\n");
        }

        DebugLogger.log(DebugLogger.Category.DIJKSTRA, "DijkstraMap", sb.toString());
    }
}
