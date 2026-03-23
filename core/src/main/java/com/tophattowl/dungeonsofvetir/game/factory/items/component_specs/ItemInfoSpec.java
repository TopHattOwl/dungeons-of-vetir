package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs;

import com.tophattowl.dungeonsofvetir.game.items.components.ItemInfoComponent;

public record ItemInfoSpec(String name) implements ComponentSpec<ItemInfoComponent>{
    @Override
    public Class<ItemInfoComponent> getComponentType() {
        return ItemInfoComponent.class;
    }

    @Override
    public ItemInfoComponent build() {
        return new ItemInfoComponent(name);
    }
}
