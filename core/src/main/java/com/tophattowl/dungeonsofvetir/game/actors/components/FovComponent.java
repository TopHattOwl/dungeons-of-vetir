package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;

import java.util.Arrays;

public class FovComponent implements Component {
    public int visionRadius;

    public boolean[][] visibleTiles;
    public boolean[][] exploredTiles;

    public FovComponent(int visionRadius, int levelWidth, int levelHeight) {
        this.visionRadius = visionRadius;
        this.visibleTiles = new boolean[levelWidth][levelHeight];
        this.exploredTiles = new boolean[levelWidth][levelHeight];
    }

    public boolean isVisible(int x, int y) {
        if (x < 0 || y < 0 || x >= this.visibleTiles.length || y >= this.visibleTiles[0].length) {
            return false;
        }
        return visibleTiles[x][y];
    }

    public boolean isExplored(int x, int y) {
        if (x < 0 || y < 0 || x >= this.exploredTiles.length || y >= this.exploredTiles[0].length) {
            return false;
        }
        return exploredTiles[x][y];
    }

    public void clearVisible() {
        for (boolean[] col : visibleTiles) {
            Arrays.fill(col, false);
        }
    }
}
