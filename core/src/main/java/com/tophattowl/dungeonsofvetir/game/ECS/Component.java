package com.tophattowl.dungeonsofvetir.game.ECS;

public interface Component {
    default void setOwner(Entity owner) {}
}
