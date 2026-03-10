package com.tophattowl.dungeonsofvetir.game.ECS;


import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public interface GameSystem {
    default void process(GameWorld world) {

    }
}
