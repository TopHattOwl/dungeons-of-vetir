package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.InventoryComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public record InventorySpec(
    int maxWeight
) implements ActorComponentSpec<InventoryComponent>{
    @Override
    public Class<InventoryComponent> getComponentType() {
        return InventoryComponent.class;
    }

    @Override
    public InventoryComponent build(Entity entity, GameWorld gameWorld) {
        return new InventoryComponent(maxWeight);
    }
}
