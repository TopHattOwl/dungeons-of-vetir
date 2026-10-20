package com.tophattowl.dungeonsofvetir.game.status;

/**
 * What an effect attaches to, part-scoped effects (bleeding on a limb) are removed when their part is destroyed
 */
public enum TargetScope {
    ENTITY,
    BODY_PART,
}
