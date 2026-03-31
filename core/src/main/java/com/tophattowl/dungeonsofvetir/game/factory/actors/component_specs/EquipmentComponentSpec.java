package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;

public record EquipmentComponentSpec(

) implements ActorComponentSpec<EquipmentComponent> {
    @Override
    public Class<EquipmentComponent> getComponentType() {
        return EquipmentComponent.class;
    }

    @Override
    public EquipmentComponent build(Entity entity) {
        EquipmentComponent comp =  new EquipmentComponent();
        BodyComponent bodyComp = entity.getComponent(BodyComponent.class);
        comp.initSlots(bodyComp);
        return comp;
    }
}
