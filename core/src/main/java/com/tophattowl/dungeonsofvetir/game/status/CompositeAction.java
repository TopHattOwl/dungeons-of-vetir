package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;

import java.util.List;

/**
 * Runs several actions in order.
 */
public record CompositeAction(
    List<EffectAction> actions
) implements EffectAction {

    @Override
    public void apply(EffectContext ctx, Entity target, StatusEffect instance) {
        for (EffectAction action : actions) {
            action.apply(ctx, target, instance);
        }
    }
}
