package com.tophattowl.dungeonsofvetir.game.items;

import com.tophattowl.dungeonsofvetir.game.items.components.ItemComponent;
import com.tophattowl.dungeonsofvetir.game.items.components.ItemInfoComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Item {
    private static int nextId = 0;

    public final int id;
    public final ItemType itemType;
    public final ItemId itemId;

    private final Map<Class<? extends ItemComponent>, ItemComponent> components = new HashMap<>();

    public Item(ItemType itemType, ItemId itemId) {
        this.itemType = itemType;
        this.itemId = itemId;
        this.id = nextId++;
    }

    public <T extends ItemComponent> Item addComponent(T component) {
        components.put(component.getClass(), component);
        return this;
    }

    public <T extends ItemComponent> Item removeComponent(Class<T> type) {
        components.remove(type);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends ItemComponent> T getComponent(Class<T> type) {
        return (T) components.get(type);
    }

    public boolean hasComponent(Class<? extends ItemComponent> type) {
        return components.containsKey(type);
    }

    @SafeVarargs
    public final boolean hasAllComponents(Class<? extends ItemComponent>... type) {
        return Arrays.stream(type)
            .allMatch(components::containsKey);
    }

    @Override
    public String toString() {
        if (hasComponent(ItemInfoComponent.class)) {
            ItemInfoComponent itemInfo = getComponent(ItemInfoComponent.class);
            return "Item: #" + id
                + " | " + "itemType: " + itemType
                + " | " + "itemId: " + itemId
                + " | " + "item name: " + itemInfo.itemName
                ;
        }
        return "Item: #" + id;
    }
}
