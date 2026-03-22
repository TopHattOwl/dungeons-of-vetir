package com.tophattowl.dungeonsofvetir.game.items;

import com.tophattowl.dungeonsofvetir.game.items.components.ItemComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Item {
    private static int nextId = 0;
    private final int id;

    private final Map<Class<? extends ItemComponent>, ItemComponent> components = new HashMap<>();

    public Item() {
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
        return "Item: #" + id;
    }
}
