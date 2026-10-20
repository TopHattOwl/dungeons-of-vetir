package com.tophattowl.dungeonsofvetir.game.stats;

/**
 * A thing a {@link Modifier} can target: either an {@link Attribute} or a {@link CombatStat}.
 * Sealed so switches over stat targets stay exhaustive as the stat system grows.
 */
public sealed interface StatRef permits Attribute, CombatStat {
}
