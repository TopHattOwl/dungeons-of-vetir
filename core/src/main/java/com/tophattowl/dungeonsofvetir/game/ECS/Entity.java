package com.tophattowl.dungeonsofvetir.game.ECS;

import com.tophattowl.dungeonsofvetir.game.ECS.components.Component;
import com.tophattowl.dungeonsofvetir.game.ECS.components.IdentityComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Entity {
    private static int nextId = 0;

    public final int id;
    private Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity() {
        this.id = nextId++;
    }

    public <T extends Component> Entity addComponent(T component) {
        components.put(component.getClass(), component);
        return this;
    }

    public <T extends Component> Entity removeComponent(Class<T> type) {
        components.remove(type);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> type) {
        return (T) components.get(type);
    }

    public boolean hasComponent(Class<? extends Component> type) {
        return components.containsKey(type);
    }

    public boolean hasAllComponents(Class<?>... type) {
        return Arrays.stream(type)
            .allMatch(t -> components.containsKey(t));
    }

    @Override
    public String toString() {
        if (hasComponent(IdentityComponent.class)) {
            return getComponent(IdentityComponent.class).name;
        }
        return "Entity #" + id;
    }

}
