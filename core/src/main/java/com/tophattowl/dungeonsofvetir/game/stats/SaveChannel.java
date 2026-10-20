package com.tophattowl.dungeonsofvetir.game.stats;

/**
 * A non-damage resistance check, rolled when a status effect would apply (not in the damage math!!) <br>
 * Values are fractions (0.1 = 10%) (derived from attributes for now)
 * <p>
 * Element resistance is separate: it reduces damage and lives as {@link CombatStat#RESIST_FIRE} style stats <br>
 * Poison is the one element that also has a save channel (since it's an element)
 */
public enum SaveChannel {
    BLEED,
    POISON,
    DISEASE,
    STUN,
    KNOCKBACK,
    CORRUPTION,
    PAIN,
    MENTAL,
}
