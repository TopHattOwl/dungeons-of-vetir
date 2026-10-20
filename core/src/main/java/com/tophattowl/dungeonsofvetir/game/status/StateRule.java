package com.tophattowl.dungeonsofvetir.game.status;

/**
 * Binds a state predicate to the effect it keeps active.
 */
public record StateRule(StatusEffectId effect, int stacks, StatePredicate applies) {
}
