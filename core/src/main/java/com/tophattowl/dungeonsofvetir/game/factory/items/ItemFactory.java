package com.tophattowl.dungeonsofvetir.game.factory.items;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemId;
import com.tophattowl.dungeonsofvetir.game.items.ItemType;
import com.tophattowl.dungeonsofvetir.game.items.components.EquipableComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.ItemInfoComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;

public class ItemFactory {

    public static Item makeTestMeleeWeapon() {
        Item item = new Item(ItemType.MELEE_WEAPON, ItemId.STEEL_LONGSWORD);
        item.addComponent(new EquipableComponent(EquipmentSlotType.MAIN_HAND))
            .addComponent(new MeleeWeaponComponent(22))
            .addComponent(new ItemInfoComponent("Test Sword"))
        ;



        return item;
    }
}
