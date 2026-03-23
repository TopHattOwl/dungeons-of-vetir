package com.tophattowl.dungeonsofvetir.game.items.components;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;


public class EquipableComponent implements ItemComponent {
    public boolean equipped = false;

    public EquipmentSlotType slot;

    public EquipableComponent(EquipmentSlotType slot) {
        this.slot = slot;
    }
}
