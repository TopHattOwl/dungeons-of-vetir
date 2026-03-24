package com.tophattowl.dungeonsofvetir.game.factory.items;

import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.ComponentSpec;

import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemId;

public class ItemFactory {

    public static Item makeItem(ItemId itemId) {
        ItemTemplate template = ItemRegistry.get(itemId);

        Item item = new Item(template.itemType(), itemId);

        for (ComponentSpec<?> spec : template.specs()) {
            item.addComponent(spec.build());
        }
        return item;
    }
}
