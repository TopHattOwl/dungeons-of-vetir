package com.tophattowl.dungeonsofvetir.game.ai;

import com.tophattowl.dungeonsofvetir.game.Direction;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class DijkstraMap {
    private int[][] distanceMap;
    private int width;
    private int height;

    public DijkstraMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.distanceMap = new int[width][height];
    }

    public void compute(Level level, GameWorld world, Point target) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                distanceMap[x][y] = Integer.MAX_VALUE;
            }
        }

        Queue<Point> queue = new ArrayDeque<>();
        distanceMap[target.x][target.y] = 0;
        queue.add(target);

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int currentDist = distanceMap[current.x][current.y];

            for (Direction dir : Direction.values()) {
                if (dir == Direction.STAY) continue;

                int nx = current.x + dir.getDx();
                int ny = current.y + dir.getDy();

                if (!level.isInBounds(nx, ny)) continue;
                if (!level.isWalkable(nx, ny)) continue;
                if (world.getEntityAt(nx, ny) != null) continue;

                if (distanceMap[nx][ny] == Integer.MAX_VALUE) {
                    distanceMap[nx][ny] = currentDist + 1;
                    queue.add(new Point(nx, ny));
                }
            }
        }
    }

    public Direction getDirection(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return Direction.STAY;
        if (distanceMap[x][y] == Integer.MAX_VALUE) return Direction.STAY;
        if (distanceMap[x][y] == 0) return Direction.STAY;

        Direction bestDir = Direction.STAY;
        int bestDist = Integer.MAX_VALUE;

        for (Direction dir : Direction.values()) {
            if (dir == Direction.STAY) continue;

            int nx = x + dir.getDx();
            int ny = y + dir.getDy();

            if (nx < 0 || nx >= width || ny < 0 || ny >= height) continue;

            int dist = distanceMap[nx][ny];
            if (dist < bestDist) {
                bestDist = dist;
                bestDir = dir;
            }
        }

        return bestDir;
    }

    public boolean isReachable(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        return distanceMap[x][y] != Integer.MAX_VALUE;
    }

    public int getDistance(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return Integer.MAX_VALUE;
        return distanceMap[x][y];
    }
}
