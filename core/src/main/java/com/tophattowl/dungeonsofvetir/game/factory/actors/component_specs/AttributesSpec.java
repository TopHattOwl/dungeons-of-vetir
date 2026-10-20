package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.AttributesComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public record AttributesSpec(
    int thew,
    int wind,
    int flesh,
    int blood,
    int grit
) implements ActorComponentSpec<AttributesComponent> {

    @Override
    public Class<AttributesComponent> getComponentType() {
        return AttributesComponent.class;
    }

    @Override
    public AttributesComponent build(Entity entity, GameWorld gameWorld) {
        return new AttributesComponent(thew, wind, flesh, blood, grit);
    }
}
