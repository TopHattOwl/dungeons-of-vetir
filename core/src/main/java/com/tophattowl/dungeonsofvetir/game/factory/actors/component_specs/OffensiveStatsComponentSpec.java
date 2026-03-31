package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.InventotyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.OffensiveStatsComponent;

public record OffensiveStatsComponentSpec(
    int baseDamage,
    float weaponDamageModifier,
    float mainHandEfficiency,
    float offHandEfficiencyModifier,
    int accuracy
) implements ActorComponentSpec<OffensiveStatsComponent>{
    @Override
    public Class<OffensiveStatsComponent> getComponentType() {
        return OffensiveStatsComponent.class;
    }

    @Override
    public OffensiveStatsComponent build(Entity entity) {
        return new OffensiveStatsComponent(
            baseDamage,
            weaponDamageModifier,
            mainHandEfficiency,
            offHandEfficiencyModifier,
            accuracy
        );
    }
}
