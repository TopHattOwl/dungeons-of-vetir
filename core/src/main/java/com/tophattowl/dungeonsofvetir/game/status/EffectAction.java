package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;

/**
 * A discrete thing an effect does at a point in time (on application, or on a periodic tick).
 * <p>
 * Actions are small, composable primitives; combinations cover new behaviours without new code
 * paths (e.g. "15% chance to bleed" = {@code ChanceAction(0.15, ApplyStatusAction(BLEEDING))}).
 * The same vocabulary is intended for abilities, spells and later reactive triggers.
 */
public sealed interface EffectAction
    permits DamageAction, HealAction, ApplyStatusAction, ChanceAction, CompositeAction {

    /**
     * @param target   the entity the effect is on
     * @param instance the effect instance firing (its bound part and source), may be null
     */
    void apply(EffectContext ctx, Entity target, StatusEffect instance);
}
