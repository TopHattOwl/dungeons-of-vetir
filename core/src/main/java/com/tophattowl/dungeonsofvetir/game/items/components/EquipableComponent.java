package com.tophattowl.dungeonsofvetir.game.items.components;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;

import java.util.List;


public class EquipableComponent implements ItemComponent {
    public boolean equipped = false;

    public List<EquipmentSlotType> slots;

    public EquipableComponent(List<EquipmentSlotType> slots) {
        this.slots = slots;
    }
}
