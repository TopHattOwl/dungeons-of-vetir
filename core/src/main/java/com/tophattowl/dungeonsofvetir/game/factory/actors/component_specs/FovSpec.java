package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.FovComponent;
import com.tophattowl.dungeonsofvetir.game.world.Level;

public record FovSpec(
    int visionRange
) implements ActorComponentSpec<FovComponent> {
    @Override
    public Class<FovComponent> getComponentType() {
        return FovComponent.class;
    }

    @Override
    public FovComponent build(Entity entity) {
        return new FovComponent(visionRange, Level.WIDTH, Level.HEIGHT);
    }
}
