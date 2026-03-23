package com.tophattowl.dungeonsofvetir.game.factory.items;

import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.ComponentSpec;
import com.tophattowl.dungeonsofvetir.game.items.ItemId;
import com.tophattowl.dungeonsofvetir.game.items.ItemType;

import java.util.List;

public record ItemTemplate(ItemType itemType, ItemId itemId, List<ComponentSpec<?>> specs) {

    public ItemTemplate(ItemType itemType, ItemId itemId, ComponentSpec<?>... specs) {
        this(itemType, itemId, List.of(specs));
    }
}
