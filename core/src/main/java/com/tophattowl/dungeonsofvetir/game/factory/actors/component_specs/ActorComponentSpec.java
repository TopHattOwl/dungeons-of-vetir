package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public interface ActorComponentSpec<T extends Component> {
    Class<T> getComponentType();

    /**
     * Builds the component. {@code gameWorld} is provided so specs can read world
     * state (e.g. the current level's dimensions for FOV).
     */
    T build(Entity entity, GameWorld gameWorld);
}
