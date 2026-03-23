package com.tophattowl.dungeonsofvetir.game.factory.items;

import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.EquipableSpec;
import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.ItemInfoSpec;
import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.MeleeWeaponSpec;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
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
                ,new MeleeWeaponSpec(22)
                ,new EquipableSpec(EquipmentSlotType.HAND_SLOT)
            )
        );
        register(
            ItemId.IRON_LONGSWORD,
            new ItemTemplate(
                ItemType.MELEE_WEAPON, ItemId.IRON_LONGSWORD
                ,new ItemInfoSpec("Iron longsword")
                ,new MeleeWeaponSpec(16)
                ,new EquipableSpec(EquipmentSlotType.HAND_SLOT)
            )
        );
    }
}
