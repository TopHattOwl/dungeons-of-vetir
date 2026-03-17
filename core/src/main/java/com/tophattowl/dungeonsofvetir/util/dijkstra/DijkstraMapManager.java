package com.tophattowl.dungeonsofvetir.util.dijkstra;

import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.Arrays;

public class DijkstraMapManager {
    public PlayerDijkstraMap playerDijkstraMap;
    private GameWorld gameWorld;

    public DijkstraMapManager(GameWorld gameWorld, PlayerDijkstraMap playerDijkstraMap) {
        this.playerDijkstraMap = playerDijkstraMap;
        this.gameWorld = gameWorld;

        playerDijkstraMap.initialize(gameWorld);
        playerDijkstraMap.calculate(gameWorld);

        for (int[] row : playerDijkstraMap.map) {
            System.out.println(Arrays.toString(row));
        }
    }
}
