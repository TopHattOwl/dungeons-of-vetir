package com.tophattowl.dungeonsofvetir.game.items.handlers;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.items.Item;

public interface EquipHandler {
    void onEquip(Entity entity, Item item);
    void onUnequip(Entity entity, Item item);
}
