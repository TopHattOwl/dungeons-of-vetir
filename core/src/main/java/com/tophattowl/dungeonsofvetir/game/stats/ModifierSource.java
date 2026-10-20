package com.tophattowl.dungeonsofvetir.game.stats;

import java.util.List;

/**
 * Anything that contributes {@link Modifier}s to an entity's stats <br>
 * Implementations are discovered by the {@link StatAggregator} (components on the entity + equipped items)
 */
public interface ModifierSource {

    /**
     * Appends this source's current modifiers to {@code out}.
     */
    void collectModifiers(List<Modifier> out);
}
