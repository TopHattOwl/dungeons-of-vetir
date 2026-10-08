package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public record EquipmentSpec(

) implements ActorComponentSpec<EquipmentComponent> {
    @Override
    public Class<EquipmentComponent> getComponentType() {
        return EquipmentComponent.class;
    }

    @Override
    public EquipmentComponent build(Entity entity, GameWorld gameWorld) {
        EquipmentComponent comp =  new EquipmentComponent();
        BodyComponent bodyComp = entity.getComponent(BodyComponent.class);
        comp.initSlots(bodyComp);
        return comp;
    }
}
