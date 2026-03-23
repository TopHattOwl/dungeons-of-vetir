package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.components.EquipableComponent;

public record EquipableSpec(EquipmentSlotType slotType) implements ComponentSpec<EquipableComponent>{
    @Override
    public Class<EquipableComponent> getComponentType() {
        return EquipableComponent.class;
    }

    @Override
    public EquipableComponent build() {
        return new EquipableComponent(slotType);
    }
}
