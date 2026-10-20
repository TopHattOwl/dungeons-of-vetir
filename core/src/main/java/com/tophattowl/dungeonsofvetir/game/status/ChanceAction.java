package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;

/**
 * Runs a nested action with a given probability.
 */
public record ChanceAction(
    float probability,
    EffectAction action
) implements EffectAction {

    @Override
    public void apply(EffectContext ctx, Entity target, StatusEffect instance) {
        if (ctx.rng().nextFloat() < probability) {
            action.apply(ctx, target, instance);
        }
    }
}
