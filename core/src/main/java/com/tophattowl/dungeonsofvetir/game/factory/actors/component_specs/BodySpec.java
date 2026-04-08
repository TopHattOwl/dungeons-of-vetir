package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;

import java.util.Map;

public record BodySpec(
    BodyTemplate bodyTemplate,
    Map<String, Integer> naturalProts
) implements ActorComponentSpec<BodyComponent> {

    public BodySpec(BodyTemplate bodyTemplate) {
        this(bodyTemplate, Map.of());
    }

    @Override
    public Class<BodyComponent> getComponentType() {
        return BodyComponent.class;
    }

    @Override
    public BodyComponent build(Entity entity) {
        int maxHp = entity.getComponent(HealthComponent.class).maxHp;
        return BodyComponentBuilder.build(bodyTemplate, maxHp,  naturalProts);
    }
}
