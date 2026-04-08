package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.RenderableComponent;

public record RenderableSpec(
    String spriteId,
    int renderOrder
) implements ActorComponentSpec<RenderableComponent>{

    @Override
    public Class<RenderableComponent> getComponentType() {
        return RenderableComponent.class;
    }

    @Override
    public RenderableComponent build(Entity entity) {
        return new RenderableComponent(spriteId, renderOrder);
    }
}
