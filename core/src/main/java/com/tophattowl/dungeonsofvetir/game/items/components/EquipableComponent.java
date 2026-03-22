package com.tophattowl.dungeonsofvetir.game.items.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlot;
import com.tophattowl.dungeonsofvetir.game.items.behavior.EquipableBehavior;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class EquipableComponent implements ItemComponent, EquipableBehavior {
    public boolean equipped = false;

    public EquipmentSlot slot;

    public EquipableComponent(EquipmentSlot slot) {
        this.slot = slot;
    }

    @Override
    public void onEquip(Entity wearer, GameWorld gameWorld) {
        equipped = true;
    }

    @Override
    public void onUnequip(Entity wearer, GameWorld gameWorld) {
        equipped = false;
    }
}
