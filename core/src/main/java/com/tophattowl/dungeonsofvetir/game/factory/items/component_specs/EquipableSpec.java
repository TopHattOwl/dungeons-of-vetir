package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.components.EquipableComponent;

import java.util.List;

public record EquipableSpec(List<EquipmentSlotType> slotTypes) implements ItemComponentSpec<EquipableComponent> {

    public EquipableSpec(EquipmentSlotType... slots) {
        this(List.of(slots));
    }

    @Override
    public Class<EquipableComponent> getComponentType() {
        return EquipableComponent.class;
    }

    @Override
    public EquipableComponent build() {
        return new EquipableComponent(slotTypes);
    }
}
