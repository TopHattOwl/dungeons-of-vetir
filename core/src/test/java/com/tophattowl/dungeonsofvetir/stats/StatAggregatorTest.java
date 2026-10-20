package com.tophattowl.dungeonsofvetir.stats;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.components.AttributesComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.DerivedStatsComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.ModifiersComponent;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemId;
import com.tophattowl.dungeonsofvetir.game.items.ItemType;
import com.tophattowl.dungeonsofvetir.game.items.components.ItemModifiersComponent;
import com.tophattowl.dungeonsofvetir.game.stats.Attribute;
import com.tophattowl.dungeonsofvetir.game.stats.CombatStat;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.stats.StatAggregator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StatAggregatorTest {

    private static final StatAggregator AGGREGATOR = new StatAggregator();

    private static Entity entity(int thew, int wind, int flesh, int blood, int grit) {
        Entity entity = new Entity();
        entity.addComponent(new AttributesComponent(thew, wind, flesh, blood, grit));
        AGGREGATOR.recompute(entity);
        return entity;
    }

    private static DerivedStatsComponent stats(Entity entity) {
        return entity.getComponent(DerivedStatsComponent.class);
    }

    @Test
    void derivesBaseStatsFromAttributes() {
        Entity entity = entity(10, 10, 10, 10, 10);
        DerivedStatsComponent derived = stats(entity);

        assertEquals(350f, derived.get(CombatStat.MAX_HP), 1e-4f);
        assertEquals(20f, derived.get(CombatStat.MELEE_DAMAGE), 1e-4f);
        assertEquals(1.02f, derived.get(CombatStat.ACCURACY), 1e-4f);
        assertEquals(0.10f, derived.get(CombatStat.DODGE_CHANCE), 1e-4f);
        assertEquals(0.10f, derived.get(CombatStat.BLOCK_CHANCE), 1e-4f);
        assertEquals(10f, derived.get(CombatStat.MAX_STAMINA), 1e-4f);
        assertEquals(1f, derived.get(CombatStat.WEAPON_DAMAGE_MODIFIER), 1e-4f);
    }

    @Test
    void createsHealthFromMaxHp() {
        Entity entity = entity(10, 10, 10, 10, 10);
        HealthComponent health = entity.getComponent(HealthComponent.class);

        assertNotNull(health);
        assertEquals(350, health.maxHp);
        assertEquals(350, health.hp);
    }

    @Test
    void modifierMathIsGlobalFlatPercentMultiply() {
        Entity entity = entity(10, 10, 10, 10, 10);
        entity.addComponent(new ModifiersComponent(
            Modifier.flat(CombatStat.MELEE_DAMAGE, 5f),
            Modifier.percent(CombatStat.MELEE_DAMAGE, 0.5f),
            Modifier.multiply(CombatStat.MELEE_DAMAGE, 2f)
        ));
        AGGREGATOR.recompute(entity);

        // (20 + 5) * 1.5 * 2
        assertEquals(75f, stats(entity).get(CombatStat.MELEE_DAMAGE), 1e-4f);
    }

    @Test
    void attributeModifiersFeedGrowth() {
        Entity entity = entity(10, 10, 10, 10, 10);
        entity.addComponent(new ModifiersComponent(Modifier.flat(Attribute.THEW, 5f)));
        AGGREGATOR.recompute(entity);

        assertEquals(15f, entity.getComponent(AttributesComponent.class).effective(Attribute.THEW), 1e-4f);
        assertEquals(30f, stats(entity).get(CombatStat.MELEE_DAMAGE), 1e-4f);
    }

    @Test
    void equippedItemModifiersApplyAndRevert() {
        Entity entity = entity(10, 10, 10, 10, 10);
        BodyPart hand = new BodyPart("hand", BodyPartType.ARM, BodyPartRole.LIMB,
            List.of(EquipmentSlotType.HAND_SLOT), 50, 0, 0.1f, 0.1f, 1f);
        BodyComponent body = new BodyComponent(List.of(hand));
        entity.addComponent(body);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);
        entity.addComponent(equipment);

        Item ring = new Item(ItemType.ARMOR, ItemId.IRON_LONGSWORD);
        ring.addComponent(new ItemModifiersComponent(Modifier.flat(CombatStat.ACCURACY, 0.2f)));

        EquipmentComponent.EquipmentSlot slot = equipment.getSpecific(hand, EquipmentSlotType.HAND_SLOT);
        slot.item = ring;
        AGGREGATOR.recompute(entity);
        assertEquals(1.22f, stats(entity).get(CombatStat.ACCURACY), 1e-4f);

        slot.item = null;
        AGGREGATOR.recompute(entity);
        assertEquals(1.02f, stats(entity).get(CombatStat.ACCURACY), 1e-4f);
    }

    @Test
    void itemSpanningTwoSlotsIsCountedOnce() {
        Entity entity = entity(10, 10, 10, 10, 10);
        BodyPart hand = new BodyPart("hand", BodyPartType.ARM, BodyPartRole.LIMB,
            List.of(EquipmentSlotType.HAND_SLOT), 50, 0, 0.1f, 0.1f, 1f);
        BodyComponent body = new BodyComponent(List.of(hand));
        entity.addComponent(body);
        EquipmentComponent equipment = new EquipmentComponent();
        entity.addComponent(equipment);

        Item twoHanded = new Item(ItemType.MELEE_WEAPON, ItemId.STEEL_LONGSWORD);
        twoHanded.addComponent(new ItemModifiersComponent(Modifier.flat(CombatStat.MELEE_DAMAGE, 10f)));

        equipment.equipment.add(new EquipmentComponent.EquipmentSlot(hand, EquipmentSlotType.HAND_SLOT, twoHanded));
        equipment.equipment.add(new EquipmentComponent.EquipmentSlot(hand, EquipmentSlotType.HAND_SLOT, twoHanded));

        AGGREGATOR.recompute(entity);

        // base 20 + 10 once, not 20
        assertEquals(30f, stats(entity).get(CombatStat.MELEE_DAMAGE), 1e-4f);
    }

    @Test
    void bodyPartMaxHpFollowsHpShare() {
        Entity entity = entity(10, 10, 10, 10, 10);
        BodyPart torso = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.5f, 1f, 1f);
        entity.addComponent(new BodyComponent(List.of(torso)));
        AGGREGATOR.recompute(entity);

        assertEquals(175, torso.maxHp);
        assertEquals(175, torso.hp);
    }

    @Test
    void healthClampsWhenMaxDrops() {
        Entity entity = entity(10, 10, 10, 10, 10);
        HealthComponent health = entity.getComponent(HealthComponent.class);
        health.hp = 300;

        // drop Flesh so max HP falls below current
        AttributesComponent attributes = entity.getComponent(AttributesComponent.class);
        attributes.setEffective(Attribute.FLESH, 1f);
        // effective is overwritten on recompute from base; simulate by editing base map
        attributes.baseValues().put(Attribute.FLESH, 1);
        AGGREGATOR.recompute(entity);

        assertEquals(health.maxHp, health.hp);
    }
}
