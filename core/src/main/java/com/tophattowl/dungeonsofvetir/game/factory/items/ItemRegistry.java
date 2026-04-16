package com.tophattowl.dungeonsofvetir.game.factory.items;

import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.EquipableSpec;
import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.ItemInfoSpec;
import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.MeleeWeaponSpec;
import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.helpers.ElementDamage;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;
import com.tophattowl.dungeonsofvetir.game.items.ItemId;
import com.tophattowl.dungeonsofvetir.game.items.ItemType;

import java.util.HashMap;
import java.util.Map;

public class ItemRegistry {

    private static final Map<ItemId, ItemTemplate> registry = new HashMap<>();

    public static ItemTemplate get(ItemId itemId) {
        return registry.get(itemId);
    }

    private static void register(ItemId itemId, ItemTemplate itemTemplate) {
        registry.put(itemId, itemTemplate);
    }

    static {
        register(
            ItemId.STEEL_LONGSWORD,
            new ItemTemplate(
                ItemType.MELEE_WEAPON, ItemId.STEEL_LONGSWORD
                ,new ItemInfoSpec("Steel longsword")
                ,new MeleeWeaponSpec(DamageType.SLASHING, ItemGripType.TWO_HANDED,
                    new ElementDamage(ElementType.PHYSICAL, 19),
                    new ElementDamage(ElementType.FIRE, 7))
                ,new EquipableSpec(EquipmentSlotType.HAND_SLOT)
            )
        );
        register(
            ItemId.IRON_LONGSWORD,
            new ItemTemplate(
                ItemType.MELEE_WEAPON, ItemId.IRON_LONGSWORD
                ,new ItemInfoSpec("Iron longsword")
                ,new MeleeWeaponSpec(DamageType.SLASHING, ItemGripType.TWO_HANDED,
                    new ElementDamage(ElementType.PHYSICAL, 9),
                    new ElementDamage(ElementType.LIGHTNING, 3))
                ,new EquipableSpec(EquipmentSlotType.HAND_SLOT)
            )
        );
        register(
            ItemId.STEEL_MACE,
            new ItemTemplate(
                ItemType.MELEE_WEAPON, ItemId.STEEL_MACE
                ,new ItemInfoSpec("Steel Mace")
                ,new MeleeWeaponSpec(DamageType.CRUSHING, ItemGripType.ONE_HANDED,
                    new ElementDamage(ElementType.PHYSICAL, 13),
                    new ElementDamage(ElementType.POISON, 5))
                ,new EquipableSpec(EquipmentSlotType.HAND_SLOT)
            )
        );
    }
}
