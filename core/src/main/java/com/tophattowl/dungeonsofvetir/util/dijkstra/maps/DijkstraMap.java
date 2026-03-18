package com.tophattowl.dungeonsofvetir.util.dijkstra.maps;


import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.Arrays;

public abstract class DijkstraMap {
    public static final int BASE_VALUE = 100;
    public static final int OBSTACLE_VALUE = 42069;
    public static final int GOAL_VALUE = 0;

    public int[][] map;

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
                int nx = x + dx, ny = y + dy;
                // skip self, walls and out of bounds
                if (nx < 0 || ny < 0 || nx >= map.length || ny >= map[x].length) continue;
                if (dx == 0 && dy == 0) continue;
                if (map[nx][ny] == OBSTACLE_VALUE) continue;

                if (map[nx][ny] < lowestValue) {
                    lowestValue = map[nx][ny];
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
