package com.tophattowl.dungeonsofvetir.game.items.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.EquipAction;
import com.tophattowl.dungeonsofvetir.game.action.SwapEquipmentAction;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.components.EquipableComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class EquipSystem implements ItemSystem {

    public Action prepareEquip(EquipAction action, GameWorld gameWorld) {
        // TODO: check if possible to equip (based on skill level, stats etc.)
        Entity entity = action.getOwner();
        BodyPart bodyPart = action.getTargetBodyPart();
        EquipmentSlotType equipmentSlotType = action.getEquipmentSlotType();
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);
        EquipmentComponent.EquipmentSlot equipmentSlot = equipmentComp.getSpecific(bodyPart, equipmentSlotType);
        Item equippedItem = equipmentSlot.item;

        if (equippedItem != null) {
            return new SwapEquipmentAction(entity, action.getItem(), equippedItem, bodyPart);
        }

        action.possible();
        return action;
    }

    public Action executeEquip(EquipAction action, GameWorld gameWorld) {
        Item item = action.getItem();
        Entity entity = action.getOwner();
        BodyPart bodyPart = action.getTargetBodyPart();
        EquipmentSlotType equipmentSlotType = action.getEquipmentSlotType();
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);

        if (item.hasComponent(EquipableComponent.class)) {
            EquipableComponent equipableComp = item.getComponent(EquipableComponent.class);
            equipableComp.equipped = true;
        }

        if (item.hasComponent(MeleeWeaponComponent.class)) {
            MeleeWeaponComponent meleeWeaponComp = item.getComponent(MeleeWeaponComponent.class);
        }

        EquipmentComponent.EquipmentSlot equipmentSlot = equipmentComp.getSpecific(bodyPart, equipmentSlotType);
        equipmentSlot.item = action.getItem();

        action.success();
        return action;
    }
}
