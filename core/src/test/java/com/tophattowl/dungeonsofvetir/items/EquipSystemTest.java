package com.tophattowl.dungeonsofvetir.items;

import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;
import com.tophattowl.dungeonsofvetir.game.items.ItemId;
import com.tophattowl.dungeonsofvetir.game.items.ItemType;
import com.tophattowl.dungeonsofvetir.game.items.components.EquipableComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import org.junit.jupiter.api.Test;
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
}
