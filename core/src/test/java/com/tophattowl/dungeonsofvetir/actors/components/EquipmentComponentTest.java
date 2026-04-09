package com.tophattowl.dungeonsofvetir.actors.components;

import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EquipmentComponentTest {

    @Test
    void initSlots_CreatesSlotsForHumanoid() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        assertFalse(equipment.equipment.isEmpty());
    }

    @Test
    void initSlots_CreatesHandSlots() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        var handSlots = equipment.getByEquipmentSlotType(com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HAND_SLOT);
        assertFalse(handSlots.isEmpty());
    }

    @Test
    void getMainHandSlot_ReturnsFirstHandSlot() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        assertNotNull(equipment.getMainHandSlot());
        assertEquals(com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HAND_SLOT, equipment.getMainHandSlot().equipmentSlotType);
    }

    @Test
    void getOffHandSlot_ReturnsSecondHandSlot() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        assertNotNull(equipment.getOffHandSlot());
        assertEquals(com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HAND_SLOT, equipment.getOffHandSlot().equipmentSlotType);
    }

    @Test
    void getMainHandSlot_AndOffHandSlot_AreDifferent() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        assertNotSame(equipment.getMainHandSlot(), equipment.getOffHandSlot());
    }

    @Test
    void getSpecific_FindsCorrectSlot() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        var head = body.getPartByName("head");
        var slot = equipment.getSpecific(head, com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HEAD);

        assertNotNull(slot);
        assertEquals(head, slot.bodyPart);
        assertEquals(com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HEAD, slot.equipmentSlotType);
    }

    @Test
    void getSpecific_ReturnsNullForInvalid() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        var slot = equipment.getSpecific(body.getPartByName("head"), com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HAND_SLOT);
        assertNull(slot);
    }

    @Test
    void getByBodyPart_ReturnsSlot() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        var head = body.getPartByName("head");
        var slot = equipment.getByBodyPart(head);

        assertNotNull(slot);
        assertEquals(head, slot.bodyPart);
    }

    @Test
    void getByBodyPart_ReturnsNullForNonEquippable() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.WORM, 120, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        var tail = body.getPartByName("tail");
        var slot = equipment.getByBodyPart(tail);
        assertNull(slot);
    }

    @Test
    void mainHand_InitializesToFirstHandSlot() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        assertNotNull(equipment.mainHand);
        assertTrue(equipment.mainHand.equippableSlots.contains(com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HAND_SLOT));
    }

    @Test
    void offHand_InitializesToSecondHandSlot() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        assertNotNull(equipment.offHand);
        assertTrue(equipment.offHand.equippableSlots.contains(com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType.HAND_SLOT));
    }

    @Test
    void equipmentSlot_ItemInitiallyNull() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        for (var slot : equipment.equipment) {
            assertNull(slot.item);
        }
    }
}
