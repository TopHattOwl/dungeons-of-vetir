package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.world.Level;

public class DungeonGenerator {

    public DungeonGenerator() {}

    public Level generateLevel(int floorNumber, long worldSeed) {
        CellularAutomataGenerator generator = new CellularAutomataGenerator(worldSeed + floorNumber);
        return generator.generate(floorNumber);
    }
}
