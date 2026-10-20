package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.stats.ModifierSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Modifiers an entity carries itself (innate/species bonuses and anything else not sourced from gear or status effects)
 * Gear modifiers live on the item's own {@code ItemModifiersComponent}
 */
public class ModifiersComponent implements Component, ModifierSource {

    private final List<Modifier> modifiers = new ArrayList<>();

    public ModifiersComponent(Modifier... modifiers) {
        this.modifiers.addAll(List.of(modifiers));
    }

    public void add(Modifier modifier) {
        modifiers.add(modifier);
    }

    public void clear() {
        modifiers.clear();
    }

    @Override
    public void collectModifiers(List<Modifier> out) {
        out.addAll(modifiers);
    }
}
