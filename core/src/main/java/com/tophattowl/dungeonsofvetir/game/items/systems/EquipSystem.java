package com.tophattowl.dungeonsofvetir.game.items.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.EquipAction;
import com.tophattowl.dungeonsofvetir.game.action.SwapEquipmentAction;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;
import com.tophattowl.dungeonsofvetir.game.items.components.ItemComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;
import com.tophattowl.dungeonsofvetir.game.items.handlers.EquipHandler;
import com.tophattowl.dungeonsofvetir.game.items.handlers.GenericEquipHandler;
import com.tophattowl.dungeonsofvetir.game.items.handlers.MeleeWeaponEquipHandler;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipSystem implements ItemSystem {

    private static final Map<Class<? extends ItemComponent>, EquipHandler> HANDLERS = new HashMap<>();
    private static final EquipHandler FALLBACK = new GenericEquipHandler();
    static {
        HANDLERS.put(MeleeWeaponComponent.class, new MeleeWeaponEquipHandler());
    }

    // ==================
    // # EQUIPPING
    // ==================

    // ----------
    // | Prepare
    // ----------
    public static Action prepareEquip(EquipAction action, GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.EQUIP_SYSTEM, "EquipSystem",
            "preparing equip"
        );
        // TODO: check if possible to equip (based on skill level, stats etc.)
        Entity entity = action.getOwner();
        BodyPart bodyPart = action.getTargetBodyPart();
        EquipmentSlotType equipmentSlotType = action.getEquipmentSlotType();
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);
        EquipmentComponent.EquipmentSlot equipmentSlot = equipmentComp.getSpecific(bodyPart, equipmentSlotType);

        if (equipmentSlot.equipmentSlotType == EquipmentSlotType.HAND_SLOT) {
            return prepareEquipHandSlot(action, gameWorld);
        } else {
            return prepareEquipArmor(action, gameWorld);
        }
    }

    private static Action prepareEquipHandSlot(EquipAction action, GameWorld gameWorld) {
        Item item = action.getItem();
        boolean isTwoHanded = item.hasComponent(MeleeWeaponComponent.class)
                && item.getComponent(MeleeWeaponComponent.class).gripType == ItemGripType.TWO_HANDED;

        if (isTwoHanded) {
            return prepareEquipTwoHanded(action, gameWorld);
        } else {
            return prepareEquipOneHanded(action, gameWorld);
        }
    }

    private static Action prepareEquipTwoHanded(EquipAction action, GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.EQUIP_SYSTEM, "EquipSystem",
            "preparing two handed equip"
        );
        Item item = action.getItem();
        Entity owner = action.getOwner();
        BodyPart bodyPart = action.getTargetBodyPart();
        EquipmentSlotType equipmentSlotType = action.getEquipmentSlotType();
        EquipmentComponent equipmentComp = owner.getComponent(EquipmentComponent.class);

        // get both hands
        List<EquipmentComponent.EquipmentSlot> handSlots = equipmentComp.getByEquipmentSlotType(equipmentSlotType);

        List<Item> itemsToUnequip = handSlots.stream()
            .filter(slot -> slot.item != null)
            .map(slot -> slot.item)
            .toList();

        if (!itemsToUnequip.isEmpty()) {
            SwapEquipmentAction swapAction = new SwapEquipmentAction(owner, item, bodyPart);
            itemsToUnequip.forEach(swapAction::addItemToUnequip);
            return swapAction;
        }

        // free to equip
        action.possible();
        return action;
    }

    private static Action prepareEquipOneHanded(EquipAction action, GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.EQUIP_SYSTEM, "EquipSystem",
            "preparing one handed equip"
        );
        Item item = action.getItem();
        Entity owner = action.getOwner();
        BodyPart bodyPart = action.getTargetBodyPart();
        EquipmentSlotType equipmentSlotType = action.getEquipmentSlotType();
        EquipmentComponent equipmentComp = owner.getComponent(EquipmentComponent.class);

        EquipmentComponent.EquipmentSlot slot = equipmentComp.getSpecific(bodyPart, equipmentSlotType);

        Item itemToUnequip = slot.item;

        if (itemToUnequip != null) {
            SwapEquipmentAction swapAction = new SwapEquipmentAction(owner, item, bodyPart);
            swapAction.addItemToUnequip(itemToUnequip);
            return swapAction;
        }

        // free to equip
        action.possible();
        return action;
    }

    private static Action prepareEquipArmor(EquipAction action, GameWorld gameWorld) {
        return null;
    }


    // ----------
    // | Execute
    // ----------
    public static Action executeEquip(EquipAction action, GameWorld gameWorld) {
        Entity entity = action.getOwner();
        BodyPart bodyPart = action.getTargetBodyPart();
        EquipmentSlotType equipmentSlotType = action.getEquipmentSlotType();
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);
        EquipmentComponent.EquipmentSlot equipmentSlot = equipmentComp.getSpecific(bodyPart, equipmentSlotType);

        if (equipmentSlot.equipmentSlotType == EquipmentSlotType.HAND_SLOT) {
            return executeEquipHandSlot(action, gameWorld);
        } else {
            return executeEquipArmor(action, gameWorld);
        }
    }

    private static Action executeEquipHandSlot(EquipAction action, GameWorld gameWorld) {
        Item item = action.getItem();
        boolean isTwoHanded = item.hasComponent(MeleeWeaponComponent.class)
            && item.getComponent(MeleeWeaponComponent.class).gripType == ItemGripType.TWO_HANDED;

        if (isTwoHanded) {
            return executeEquipTwoHanded(action, gameWorld);
        } else {
            return executeEquipOneHanded(action, gameWorld);
        }
    }

    private static Action executeEquipTwoHanded(EquipAction action, GameWorld gameWorld) {
        Item item = action.getItem();
        Entity entity = action.getOwner();

        BodyPart bodyPart = action.getTargetBodyPart();
        EquipmentSlotType equipmentSlotType = action.getEquipmentSlotType();
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);

        // get both hands
        List<EquipmentComponent.EquipmentSlot> handSlots = equipmentComp.getByEquipmentSlotType(equipmentSlotType);

        handSlots.forEach(slot -> slot.item = item);

        for (EquipHandler handler : resolveHandlers(item)) {
            handler.onEquip(entity, item);
        }
        action.success();
        return action;
    }

    private static Action executeEquipOneHanded(EquipAction action, GameWorld gameWorld) {
        Item item = action.getItem();
        Entity entity = action.getOwner();

        BodyPart bodyPart = action.getTargetBodyPart();
        EquipmentSlotType equipmentSlotType = action.getEquipmentSlotType();
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);
        EquipmentComponent.EquipmentSlot slot = equipmentComp.getSpecific(bodyPart, equipmentSlotType);

        slot.item = item;

        for (EquipHandler handler : resolveHandlers(item)) {
            handler.onEquip(entity, item);
        }

        action.success();
        return action;
    }

    private static Action executeEquipArmor(EquipAction action, GameWorld gameWorld) {
        return null;
    }


    // ==================
    // # UNEQUIPPING
    // ==================

    // ==================
    // # SWAPPING
    // ==================


    /**
     * returns all EquipHandlers that need to be used on Item
     */
    private static List<EquipHandler> resolveHandlers(Item item) {
        List<EquipHandler> matched = new ArrayList<>();
        for (Map.Entry<Class<? extends ItemComponent>, EquipHandler> entry : HANDLERS.entrySet()) {
            if (item.hasComponent(entry.getKey())) {
                matched.add(entry.getValue());
            }
        }
        if (matched.isEmpty()) matched.add(FALLBACK);
        return matched;
    }
}
