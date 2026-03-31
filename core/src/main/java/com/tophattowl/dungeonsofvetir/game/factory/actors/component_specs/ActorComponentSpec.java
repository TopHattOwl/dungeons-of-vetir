package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;

public interface ActorComponentSpec<T extends Component> {
    Class<T> getComponentType();
    T build(Entity entity);
}
