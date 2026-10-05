package com.tophattowl.dungeonsofvetir.game.items.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.EquipAction;
import com.tophattowl.dungeonsofvetir.game.action.SwapEquipmentAction;
import com.tophattowl.dungeonsofvetir.game.action.UnequipAction;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent.EquipmentSlot;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.item.EquipmentChangedEvent;
import com.tophattowl.dungeonsofvetir.game.factory.action.ActionFactory;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;
import com.tophattowl.dungeonsofvetir.game.items.components.ItemComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;
import com.tophattowl.dungeonsofvetir.game.items.handlers.EquipHandler;
import com.tophattowl.dungeonsofvetir.game.items.handlers.MeleeWeaponEquipHandler;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.List;

/**
 * Handles equipping, unequipping and swapping items on an entity's body slots
 * <p>
 * A two-handed weapon occupies every HAND_SLOT on the body <br>
 * everything else occupies a single slot
 */
public class EquipSystem implements ItemSystem {

    private static final List<EquipHandler<?>> HANDLERS = List.of(
        new MeleeWeaponEquipHandler()
    );

    // ==================
    // # EQUIPPING
    // ==================
    public static Action prepareEquip(EquipAction action, GameWorld gameWorld) {
        Entity entity = action.getOwner();
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);

        if (isTwoHandedHandEquip(action.getItem(), action.getEquipmentSlotType())) {
            return prepareEquipTwoHanded(action, equipmentComp);
        }
        return prepareEquipSingleSlot(action, equipmentComp);
    }

    public static Action executeEquip(EquipAction action, GameWorld gameWorld) {
        Entity entity = action.getOwner();

        if (isTwoHandedHandEquip(action.getItem(), action.getEquipmentSlotType())) {
            equipTwoHanded(entity, action.getItem());
        } else {
            equipSingleSlot(entity, action.getItem(), action.getTargetBodyPart(), action.getEquipmentSlotType());
        }
        action.success();
        return action;
    }

    private static Action prepareEquipSingleSlot(EquipAction action, EquipmentComponent equipmentComp) {
        EquipmentSlot slot = equipmentComp.getSpecific(
            action.getTargetBodyPart(), action.getEquipmentSlotType()
        );

        // invalid slot: leave the action not-possible instead of crashing
        if (slot == null) return action;

        if (slot.item != null) {
            SwapEquipmentAction swapAction = ActionFactory.createSwapEquipmentAction(
                action.getOwner(), action.getItem(),
                action.getTargetBodyPart(), action.getEquipmentSlotType()
            );
            swapAction.addItemToUnequip(slot.item);
            swapAction.possible();
            return swapAction;
        }

        action.possible();
        return action;
    }

    private static Action prepareEquipTwoHanded(EquipAction action, EquipmentComponent equipmentComp) {
        List<Item> itemsToUnequip = equipmentComp
            .getByEquipmentSlotType(EquipmentSlotType.HAND_SLOT).stream()
            .filter(slot -> slot.item != null)
            .map(slot -> slot.item)
            .toList();

        if (!itemsToUnequip.isEmpty()) {
            SwapEquipmentAction swapAction = ActionFactory.createSwapEquipmentAction(
                action.getOwner(), action.getItem(),
                action.getTargetBodyPart(), EquipmentSlotType.HAND_SLOT
            );
            itemsToUnequip.forEach(swapAction::addItemToUnequip);
            swapAction.possible();
            return swapAction;
        }

        action.possible();
        return action;
    }

    // ==================
    // # UNEQUIPPING
    // ==================
    public static Action prepareUnequip(UnequipAction action, GameWorld gameWorld) {
        EquipmentComponent equipmentComp = action.getOwner().getComponent(EquipmentComponent.class);
        if (equipmentComp != null && equipmentComp.isEquipped(action.getItem())) {
            action.possible();
        }
        return action;
    }

    public static Action executeUnequip(UnequipAction action, GameWorld gameWorld) {
        unequipItem(action.getOwner(), action.getItem());
        action.success();
        return action;
    }

    // ==================
    // # SWAPPING
    // ==================
    public static Action prepareSwap(SwapEquipmentAction action, GameWorld gameWorld) {
        EquipmentComponent equipmentComp = action.getOwner().getComponent(EquipmentComponent.class);
        if (equipmentComp == null) return action;

        for (Item item : action.getItemsToUnequip()) {
            if (!equipmentComp.isEquipped(item)) {
                return action; // cannot swap items that are not worn
            }
        }

        if (isTwoHandedHandEquip(action.getItem(), action.getEquipmentSlotType())) {
            action.possible();
            return action;
        }
        if (equipmentComp.getSpecific(action.getTargetBodyPart(), action.getEquipmentSlotType()) == null) {
            return action;
        }
        action.possible();
        return action;
    }

    public static Action executeSwap(SwapEquipmentAction action, GameWorld gameWorld) {
        Entity entity = action.getOwner();

        for (Item item : action.getItemsToUnequip()) {
            unequipItem(entity, item);
        }

        if (isTwoHandedHandEquip(action.getItem(), action.getEquipmentSlotType())) {
            equipTwoHanded(entity, action.getItem());
        } else {
            equipSingleSlot(entity, action.getItem(), action.getTargetBodyPart(), action.getEquipmentSlotType());
        }

        action.success();
        return action;
    }

    // ==================
    // # SLOT HELPERS
    // ==================
    private static void equipSingleSlot(Entity entity, Item item, BodyPart bodyPart, EquipmentSlotType slotType) {
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);
        if (equipmentComp == null) return;

        EquipmentSlot slot = equipmentComp.getSpecific(bodyPart, slotType);
        if (slot == null) {
            DebugLogger.log(DebugLogger.Category.EQUIP_SYSTEM, DebugLogger.Level.WARNING,
                "EquipSystem", "No equipment slot " + slotType + " on " + bodyPart.name);
            return;
        }

        slot.item = item;
        runHandlers(entity, item, true);
        EventBus.emit(new EquipmentChangedEvent(entity, item, true));
    }

    private static void equipTwoHanded(Entity entity, Item item) {
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);
        if (equipmentComp == null) return;

        for (EquipmentSlot slot : equipmentComp.getByEquipmentSlotType(EquipmentSlotType.HAND_SLOT)) {
            slot.item = item;
        }
        runHandlers(entity, item, true);
        EventBus.emit(new EquipmentChangedEvent(entity, item, true));
    }

    private static void unequipItem(Entity entity, Item item) {
        EquipmentComponent equipmentComp = entity.getComponent(EquipmentComponent.class);
        if (equipmentComp == null || !equipmentComp.isEquipped(item)) return;

        equipmentComp.clearItem(item);
        runHandlers(entity, item, false);
        EventBus.emit(new EquipmentChangedEvent(entity, item, false));
    }

    private static boolean isTwoHandedHandEquip(Item item, EquipmentSlotType slotType) {
        return slotType == EquipmentSlotType.HAND_SLOT
            && item.hasComponent(MeleeWeaponComponent.class)
            && item.getComponent(MeleeWeaponComponent.class).getGripType() == ItemGripType.TWO_HANDED;
    }

    /**
     * Runs every handler whose component the item has
     */
    private static void runHandlers(Entity entity, Item item, boolean equipping) {
        for (EquipHandler<?> handler : HANDLERS) {
            dispatch(handler, entity, item, equipping);
        }
    }

    private static <T extends ItemComponent> void dispatch(EquipHandler<T> handler,
                                                           Entity entity, Item item, boolean equipping) {
        T component = item.getComponent(handler.componentType());
        if (component == null) return;

        if (equipping) {
            handler.onEquip(entity, item, component);
        } else {
            handler.onUnequip(entity, item, component);
        }
    }
}
