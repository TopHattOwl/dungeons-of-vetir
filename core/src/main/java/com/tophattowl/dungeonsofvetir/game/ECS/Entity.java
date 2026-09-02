package com.tophattowl.dungeonsofvetir.game.ECS;

import com.badlogic.gdx.utils.Disposable;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;

import java.util.*;

public class Entity implements Disposable {
    private static int nextId = 0;

    public final int id;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity() {
        this.id = nextId++;
    }

    public <T extends Component> Entity addComponent(T component) {
        components.put(component.getClass(), component);
        if (component instanceof OwnedComponent owned) {
            owned.setOwner(this);
        }
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

    @SafeVarargs
    public final boolean hasAllComponents(Class<? extends Component>... type) {
        return Arrays.stream(type)
            .allMatch(components::containsKey);
    }

    @Override
    public String toString() {
        if (hasComponent(IdentityComponent.class)) {
            return "Entity ID: #" + id + "\n" + getComponent(IdentityComponent.class);
        }
        return "Entity #" + id;
    }

    public String getAllInfo() {
        StringBuilder sb = new StringBuilder();

        for (Component component : components.values()) {
            sb.append(component.toString()).append("\n");
        }
        return sb.toString();
    }

    public void dispose() {
        for (Component comp : components.values()) {
            if (comp instanceof Disposable disposeComp) {
                disposeComp.dispose();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
