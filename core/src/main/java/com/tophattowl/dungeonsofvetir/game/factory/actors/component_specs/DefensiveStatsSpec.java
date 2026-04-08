package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.DefensiveStatsComponent;

public record DefensiveStatsSpec(
    int evasion,
    float counterChance,
    float blockChance
) implements ActorComponentSpec<DefensiveStatsComponent> {
    @Override
    public Class<DefensiveStatsComponent> getComponentType() {
        return DefensiveStatsComponent.class;
    }

    @Override
    public DefensiveStatsComponent build(Entity entity) {
        BodyComponent bodyComp = entity.getComponent(BodyComponent.class);
        return new DefensiveStatsComponent(evasion, counterChance, blockChance, bodyComp);
    }
}
