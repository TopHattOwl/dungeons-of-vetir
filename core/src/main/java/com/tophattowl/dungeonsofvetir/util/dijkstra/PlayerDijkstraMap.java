package com.tophattowl.dungeonsofvetir.util.dijkstra;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;


public class PlayerDijkstraMap extends DijkstraMap {
    public PlayerDijkstraMap(int width, int height) {
        super(width, height);
    }

    @Override
    public void calculate(GameWorld gameWorld) {
        Level currentLevel = gameWorld.getCurrentLevel();
        boolean hasChanged = false;
        do {
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[x].length; y++) {
                    if (map[x][y] == WALL_VALUE) continue;

                    hasChanged = checkNeighbors(x, y);
                }
            }
        } while (hasChanged);
    }

    private boolean checkNeighbors(int x, int y) {

        int lowestValue = map[x][y];
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                // skip self and walls
                if (dx == 0 && dy == 0) continue;
                if (map[x+dx][y+dy] == WALL_VALUE) continue;

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

    @Override
    public void initialize(GameWorld gameWorld) {
        Level level = gameWorld.getCurrentLevel();

        Entity player = gameWorld.getPlayer();
        Point playerPos = player.getComponent(PositionComponent.class).getPosition();
        int playerX = playerPos.x;
        int playerY = playerPos.y;

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {

                if (x == playerX && y == playerY) {
                    map[x][y] = GOAL_VALUE;
                    continue;
                }

                if (level.isWalkable(x, y)) {
                    map[x][y] = BASE_VALUE;
                    continue;
                }

                map[x][y] = WALL_VALUE;
            }
        }
    }
}
