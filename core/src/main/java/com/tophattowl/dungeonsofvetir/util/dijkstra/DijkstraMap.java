package com.tophattowl.dungeonsofvetir.util.dijkstra;


import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.Arrays;

public abstract class DijkstraMap {
    protected static final int BASE_VALUE = 100;
    protected static final int WALL_VALUE = Integer.MAX_VALUE;
    protected static final int GOAL_VALUE = 0;

    protected int[][] map;

    public DijkstraMap(int width, int height) {
        map = new int[width][height];
    }

    public void initialize(GameWorld gameWorld) {}

    public void calculate(GameWorld gameWorld) {}


}
