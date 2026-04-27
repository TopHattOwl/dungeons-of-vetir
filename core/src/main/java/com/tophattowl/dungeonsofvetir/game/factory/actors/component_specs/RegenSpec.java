package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.RegenComponent;

public record RegenSpec(
    int hpPerTick,
    int tickThreshold,
    float bodyPartHealEfficiency
) implements ActorComponentSpec<RegenComponent> {

    public RegenSpec(int hpPerTick) {
        this(hpPerTick, 5, 0.1f);
    }

    @Override
    public Class<RegenComponent> getComponentType() {
        return RegenComponent.class;
    }

    @Override
    public RegenComponent build(Entity entity) {
        return new RegenComponent(hpPerTick, tickThreshold, bodyPartHealEfficiency);
    }
}
