package com.tophattowl.dungeonsofvetir.util.dijkstra.maps;

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
    public void initialize(GameWorld gameWorld) {
        Level level = gameWorld.getCurrentLevel();

        Entity player = gameWorld.getPlayer();
        Point playerPos = player.getComponent(PositionComponent.class).getPosition();
        int playerX = playerPos.x;
        int playerY = playerPos.y;

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                initTile(x, y, playerX, playerY, level);
            }
        }
    }

    private void initTile(int x, int y, int playerX, int playerY, Level level) {
        if (x == playerX && y == playerY) {
            map[x][y] = GOAL_VALUE;
            return;
        }

        if (level.isWalkable(x, y)) {
            map[x][y] = BASE_VALUE;
            return;
        }

        map[x][y] = OBSTACLE_VALUE;
    }
}
