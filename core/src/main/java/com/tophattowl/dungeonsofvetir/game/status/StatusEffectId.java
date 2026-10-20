package com.tophattowl.dungeonsofvetir.game.status;

public enum StatusEffectId {
    // applied effects
    STUNNED,
    BURNING,
    BLEEDING,
    POISONED,
    REGENERATING,

    // threshold/state effects
    WOUNDED,
    CONCUSSED,
    CRIPPLED,

    // internal suppression markers (regen)
    RECENTLY_WOUNDED,
    EXERTED,
}
