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
        Entity player = gameWorld.getPlayer();
        Point playerPos = player.getComponent(PositionComponent.class).getPosition();
        int playerX = playerPos.x;
        int playerY = playerPos.y;

        map[playerX][playerY] = 0;

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
