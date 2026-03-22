package com.tophattowl.dungeonsofvetir.game.items.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.EquipAction;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.components.EquipableComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class EquipSystem implements ItemSystem {

    public Action prepareEquip(EquipAction action, GameWorld gameWorld) {
        // TODO: check if possible to equip (based on skill level, stats etc.)

        action.possible();
        return action;
    }

    public Action executeEquip(EquipAction action, GameWorld gameWorld) {
        Item item = action.getItem();
        Entity entity = action.getOwner();

        item.getComponent(EquipableComponent.class).onEquip(entity, gameWorld);
        item.getComponent(MeleeWeaponComponent.class).onEquip(entity, gameWorld);

        return null;
    }
}
