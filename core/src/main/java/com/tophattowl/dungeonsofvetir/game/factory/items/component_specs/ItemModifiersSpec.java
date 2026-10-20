package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs;

import com.tophattowl.dungeonsofvetir.game.items.components.ItemModifiersComponent;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;

public record ItemModifiersSpec(
    Modifier... modifiers
) implements ItemComponentSpec<ItemModifiersComponent> {

    @Override
    public Class<ItemModifiersComponent> getComponentType() {
        return ItemModifiersComponent.class;
    }

    @Override
    public ItemModifiersComponent build() {
        return new ItemModifiersComponent(modifiers);
    }
}
