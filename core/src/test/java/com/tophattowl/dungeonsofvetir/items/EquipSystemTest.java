package com.tophattowl.dungeonsofvetir.items;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.EquipAction;
import com.tophattowl.dungeonsofvetir.game.action.SwapEquipmentAction;
import com.tophattowl.dungeonsofvetir.game.action.UnequipAction;
import com.tophattowl.dungeonsofvetir.game.factory.action.ActionFactory;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;
import com.tophattowl.dungeonsofvetir.game.items.ItemId;
import com.tophattowl.dungeonsofvetir.game.items.ItemType;
import com.tophattowl.dungeonsofvetir.game.items.components.EquipableComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;
import com.tophattowl.dungeonsofvetir.game.items.systems.EquipSystem;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.item.EquipmentChangedEvent;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EquipSystemTest {

    private static BodyComponent createHumanoidBody() {
        return BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
    }

    private static Item createOneHandedWeapon() {
        Item item = new Item(ItemType.MELEE_WEAPON, ItemId.STEEL_MACE);
        item.addComponent(new MeleeWeaponComponent(DamageType.CRUSHING, ItemGripType.ONE_HANDED));
        item.addComponent(new EquipableComponent(List.of(com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HAND_SLOT)));
        return item;
    }

    private static Item createTwoHandedWeapon() {
        Item item = new Item(ItemType.MELEE_WEAPON, ItemId.IRON_LONGSWORD);
        item.addComponent(new MeleeWeaponComponent(DamageType.SLASHING, ItemGripType.TWO_HANDED));
        item.addComponent(new EquipableComponent(List.of(com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HAND_SLOT)));
        return item;
    }

    @Test
    void meleeWeaponComponent_CreatesCorrectly() {
        MeleeWeaponComponent comp = new MeleeWeaponComponent(DamageType.SLASHING, ItemGripType.ONE_HANDED);
        assertEquals(DamageType.SLASHING, comp.getDamageType());
        assertEquals(ItemGripType.ONE_HANDED, comp.getGripType());
    }

    @Test
    void meleeWeaponComponent_TwoHandedDetection() {
        MeleeWeaponComponent oneHanded = new MeleeWeaponComponent(DamageType.SLASHING, ItemGripType.ONE_HANDED);
        MeleeWeaponComponent twoHanded = new MeleeWeaponComponent(DamageType.SLASHING, ItemGripType.TWO_HANDED);
        MeleeWeaponComponent flexible = new MeleeWeaponComponent(DamageType.SLASHING, ItemGripType.FLEXIBLE);

        assertEquals(ItemGripType.ONE_HANDED, oneHanded.getGripType());
        assertEquals(ItemGripType.TWO_HANDED, twoHanded.getGripType());
        assertEquals(ItemGripType.FLEXIBLE, flexible.getGripType());
    }

    @Test
    void equipmentComponent_HasMainAndOffHandSlots() {
        BodyComponent body = createHumanoidBody();
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        assertNotNull(equipment.getMainHandSlot());
        assertNotNull(equipment.getOffHandSlot());
    }

    @Test
    void equipmentComponent_MainHandSlotInitiallyEmpty() {
        BodyComponent body = createHumanoidBody();
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        assertNull(equipment.getMainHandSlot().item);
    }

    @Test
    void item_CanBeCreated() {
        Item item = createOneHandedWeapon();
        assertNotNull(item);
        assertEquals(ItemType.MELEE_WEAPON, item.itemType);
        assertEquals(ItemId.STEEL_MACE, item.itemId);
    }

    @Test
    void item_HasMeleeWeaponComponent() {
        Item item = createOneHandedWeapon();
        assertTrue(item.hasComponent(MeleeWeaponComponent.class));
        MeleeWeaponComponent weapon = item.getComponent(MeleeWeaponComponent.class);
        assertNotNull(weapon);
    }

    @Test
    void item_HasEquipableComponent() {
        Item item = createOneHandedWeapon();
        assertTrue(item.hasComponent(EquipableComponent.class));
    }

    @Test
    void twoHandedWeapon_CanBeCreated() {
        Item item = createTwoHandedWeapon();
        assertNotNull(item);
        assertTrue(item.hasComponent(MeleeWeaponComponent.class));
        MeleeWeaponComponent weapon = item.getComponent(MeleeWeaponComponent.class);
        assertEquals(ItemGripType.TWO_HANDED, weapon.getGripType());
    }

    @Test
    void itemId_AllValues() {
        assertNotNull(ItemId.IRON_LONGSWORD);
        assertNotNull(ItemId.STEEL_LONGSWORD);
        assertNotNull(ItemId.STEEL_DAGGER);
        assertNotNull(ItemId.STEEL_MACE);
        assertNotNull(ItemId.IRON_SPEAR);
    }

    @Test
    void itemType_AllValues() {
        assertNotNull(ItemType.MELEE_WEAPON);
        assertNotNull(ItemType.RANGED_WEAPON);
        assertNotNull(ItemType.SHIELD);
        assertNotNull(ItemType.ARMOR);
        assertNotNull(ItemType.POTION);
        assertNotNull(ItemType.POWDER);
    }

    @Test
    void gripType_AllValues() {
        assertNotNull(ItemGripType.ONE_HANDED);
        assertNotNull(ItemGripType.TWO_HANDED);
        assertNotNull(ItemGripType.FLEXIBLE);
    }

    // ==================
    // # equip / unequip / swap flow
    // ==================
    private static Entity entityWithBody(BodyComponent body) {
        Entity entity = new Entity();
        entity.addComponent(body);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);
        entity.addComponent(equipment);
        return entity;
    }

    private static Action prepareAndExecuteEquip(Entity entity, Item item, BodyPart part, EquipmentSlotType slotType) {
        EquipAction action = ActionFactory.createEquipAction(entity, item, part, slotType);
        Action prepared = EquipSystem.prepareEquip(action, null);
        if (prepared.notPossible()) return prepared;
        return prepared instanceof SwapEquipmentAction swap
            ? EquipSystem.executeSwap(swap, null)
            : EquipSystem.executeEquip((EquipAction) prepared, null);
    }

    @Test
    void equipOneHanded_FillsMainHandSlot() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);
        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        Item weapon = createOneHandedWeapon();

        BodyPart mainHand = equipment.getMainHandSlot().bodyPart;
        prepareAndExecuteEquip(entity, weapon, mainHand, EquipmentSlotType.HAND_SLOT);

        assertSame(weapon, equipment.getMainHandSlot().item);
    }

    @Test
    void equipIntoOccupiedSlot_ProducesSwapAction() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);
        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        BodyPart mainHand = equipment.getMainHandSlot().bodyPart;

        Item first = createOneHandedWeapon();
        prepareAndExecuteEquip(entity, first, mainHand, EquipmentSlotType.HAND_SLOT);
        assertSame(first, equipment.getMainHandSlot().item);

        Item second = createOneHandedWeapon();
        EquipAction action = ActionFactory.createEquipAction(entity, second, mainHand, EquipmentSlotType.HAND_SLOT);
        Action prepared = EquipSystem.prepareEquip(action, null);

        assertInstanceOf(SwapEquipmentAction.class, prepared);
        assertEquals(List.of(first), ((SwapEquipmentAction) prepared).getItemsToUnequip());
    }

    @Test
    void executeSwap_ReplacesEquippedItem() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);
        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        BodyPart mainHand = equipment.getMainHandSlot().bodyPart;

        Item first = createOneHandedWeapon();
        prepareAndExecuteEquip(entity, first, mainHand, EquipmentSlotType.HAND_SLOT);

        Item second = createOneHandedWeapon();
        prepareAndExecuteEquip(entity, second, mainHand, EquipmentSlotType.HAND_SLOT);

        assertSame(second, equipment.getMainHandSlot().item);
        assertFalse(equipment.isEquipped(first));
    }

    @Test
    void twoHanded_FillsBothHands_AndUnequipClearsAll() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);
        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        BodyPart mainHand = equipment.getMainHandSlot().bodyPart;

        Item twoHanded = createTwoHandedWeapon();
        prepareAndExecuteEquip(entity, twoHanded, mainHand, EquipmentSlotType.HAND_SLOT);

        assertSame(twoHanded, equipment.getMainHandSlot().item);
        assertSame(twoHanded, equipment.getOffHandSlot().item);

        UnequipAction unequip = ActionFactory.createUnequipAction(entity, twoHanded);
        Action prepared = EquipSystem.prepareUnequip(unequip, null);
        assertFalse(prepared.notPossible());
        EquipSystem.executeUnequip(unequip, null);

        assertNull(equipment.getMainHandSlot().item);
        assertNull(equipment.getOffHandSlot().item);
    }

    @Test
    void unequip_ItemNotEquipped_IsNotPossible() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);

        UnequipAction unequip = ActionFactory.createUnequipAction(entity, createOneHandedWeapon());
        assertTrue(EquipSystem.prepareUnequip(unequip, null).notPossible());
    }

    @Test
    void equipArmor_FillsArmorSlot() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);
        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        BodyPart head = body.getPartByName("head");

        Item helm = new Item(ItemType.ARMOR, ItemId.STEEL_MACE);
        helm.addComponent(new EquipableComponent(List.of(EquipmentSlotType.HEAD)));

        prepareAndExecuteEquip(entity, helm, head, EquipmentSlotType.HEAD);

        assertSame(helm, equipment.getSpecific(head, EquipmentSlotType.HEAD).item);
    }

    // ==================
    // # equipment changed events
    // ==================
    private static List<EquipmentChangedEvent> capture(Runnable action) {
        List<EquipmentChangedEvent> events = new ArrayList<>();
        EventBus.ListenerHandle<EquipmentChangedEvent> handle =
            EventBus.on(EquipmentChangedEvent.class, events::add);
        try {
            action.run();
        } finally {
            EventBus.off(handle);
        }
        return events;
    }

    @Test
    void equip_EmitsEquipmentChangedEvent() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);
        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        Item weapon = createOneHandedWeapon();
        BodyPart mainHand = equipment.getMainHandSlot().bodyPart;

        List<EquipmentChangedEvent> events =
            capture(() -> prepareAndExecuteEquip(entity, weapon, mainHand, EquipmentSlotType.HAND_SLOT));

        assertEquals(1, events.size());
        assertSame(weapon, events.get(0).item());
        assertTrue(events.get(0).equipped());
        assertSame(entity, events.get(0).entity());
    }

    @Test
    void unequip_EmitsEquipmentChangedEvent() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);
        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        Item weapon = createOneHandedWeapon();
        BodyPart mainHand = equipment.getMainHandSlot().bodyPart;
        prepareAndExecuteEquip(entity, weapon, mainHand, EquipmentSlotType.HAND_SLOT);

        UnequipAction unequip = ActionFactory.createUnequipAction(entity, weapon);
        List<EquipmentChangedEvent> events =
            capture(() -> EquipSystem.executeUnequip(unequip, null));

        assertEquals(1, events.size());
        assertSame(weapon, events.get(0).item());
        assertFalse(events.get(0).equipped());
    }

    @Test
    void swap_EmitsUnequipThenEquipEvents() {
        BodyComponent body = createHumanoidBody();
        Entity entity = entityWithBody(body);
        EquipmentComponent equipment = entity.getComponent(EquipmentComponent.class);
        BodyPart mainHand = equipment.getMainHandSlot().bodyPart;

        Item first = createOneHandedWeapon();
        prepareAndExecuteEquip(entity, first, mainHand, EquipmentSlotType.HAND_SLOT);

        Item second = createOneHandedWeapon();
        List<EquipmentChangedEvent> events =
            capture(() -> prepareAndExecuteEquip(entity, second, mainHand, EquipmentSlotType.HAND_SLOT));

        assertEquals(2, events.size());
        assertSame(first, events.get(0).item());
        assertFalse(events.get(0).equipped());
        assertSame(second, events.get(1).item());
        assertTrue(events.get(1).equipped());
    }
}


