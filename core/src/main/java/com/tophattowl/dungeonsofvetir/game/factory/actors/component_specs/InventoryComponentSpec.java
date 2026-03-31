package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.InventotyComponent;

public record InventoryComponentSpec(
    int maxWeight
) implements ActorComponentSpec<InventotyComponent>{
    @Override
    public Class<InventotyComponent> getComponentType() {
        return InventotyComponent.class;
    }

    @Override
    public InventotyComponent build(Entity entity) {
        return new InventotyComponent(maxWeight);
    }
}
