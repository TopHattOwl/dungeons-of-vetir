package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.world.Level;

public class DungeonGenerator {
    private final long seed;

    public DungeonGenerator(long seed) {
        this.seed = seed;
    }

    public DungeonGenerator() {
        this.seed = System.currentTimeMillis();
    }

    public Level generateLevel(int floorNumber) {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(seed + floorNumber);
        return generator.generate(floorNumber);
    }
}
