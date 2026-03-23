package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs;

import com.tophattowl.dungeonsofvetir.game.items.components.ItemComponent;

public interface ComponentSpec<T extends ItemComponent> {
    Class<T> getComponentType();
    T build();
}
