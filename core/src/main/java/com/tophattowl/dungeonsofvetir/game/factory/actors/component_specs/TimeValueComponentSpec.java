package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;

public record TimeValueComponentSpec(
    float baseSpeed
) implements ActorComponentSpec<TimeValueComponent>{

    public TimeValueComponentSpec() {
        this(1.0f);
    }

    @Override
    public Class<TimeValueComponent> getComponentType() {
        return TimeValueComponent.class;
    }

    @Override
    public TimeValueComponent build(Entity entity) {
        return new TimeValueComponent(baseSpeed);
    }
}
