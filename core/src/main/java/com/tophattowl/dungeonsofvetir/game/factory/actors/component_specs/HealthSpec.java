package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public record HealthSpec(
    int maxHp
) implements ActorComponentSpec<HealthComponent> {
    @Override
    public Class<HealthComponent> getComponentType() {
        return HealthComponent.class;
    }

    @Override
    public HealthComponent build(Entity entity, GameWorld gameWorld) {
        return new HealthComponent(maxHp);
    }
}
