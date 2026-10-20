package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;

/**
 * Applies another status effect to the target.
 */
public record ApplyStatusAction(
    StatusEffectId effect,
    int stacks,
    int durationTurns
) implements EffectAction {

    @Override
    public void apply(EffectContext ctx, Entity target, StatusEffect instance) {
        if (target == null) return;
        String source = instance == null ? null : instance.sourceId();
        ctx.world().statusEffects().apply(target, effect, source, stacks, durationTurns);
    }
}
