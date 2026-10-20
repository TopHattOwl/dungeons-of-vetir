package com.tophattowl.dungeonsofvetir.game.items.components;

import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.stats.ModifierSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Stat modifiers granted by an equipped item <br>
 * Collected by the {@code StatAggregator} while the item occupies an equipment slot <br>
 * counted once even if it spans several slots
 */
public class ItemModifiersComponent implements ItemComponent, ModifierSource {

    private final List<Modifier> modifiers = new ArrayList<>();

    public ItemModifiersComponent(Modifier... modifiers) {
        this.modifiers.addAll(List.of(modifiers));
    }

    @Override
    public void collectModifiers(List<Modifier> out) {
        out.addAll(modifiers);
    }
}
