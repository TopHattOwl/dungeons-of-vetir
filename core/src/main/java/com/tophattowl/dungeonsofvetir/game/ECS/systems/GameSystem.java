package com.tophattowl.dungeonsofvetir.game.ECS.systems;


import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public interface GameSystem {
    default void process(GameWorld world) {

    }
}
