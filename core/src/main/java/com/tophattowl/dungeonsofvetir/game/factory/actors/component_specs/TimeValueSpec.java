package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public record TimeValueSpec(
    float baseSpeed
) implements ActorComponentSpec<TimeValueComponent>{

    public TimeValueSpec() {
        this(1.0f);
    }

    @Override
    public Class<TimeValueComponent> getComponentType() {
        return TimeValueComponent.class;
    }

    @Override
    public TimeValueComponent build(Entity entity, GameWorld gameWorld) {
        return new TimeValueComponent(baseSpeed);
    }
}
