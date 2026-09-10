package com.tophattowl.dungeonsofvetir.game.actors.body;

/**
 * Condition of a body part
 * <p>
 * Thresholds are lower bounds
 * <p>
 * DESTROYED is only reached at exactly 0 hp and is handled separately, so its threshold is unused.
 */
public enum BodyPartStatus {
    HEALTHY(0.8f),
    INJURED(0.5f),
    CRIPPLED(0.2f),
    DESTROYED(0.0f);

    public final float threshold;

    BodyPartStatus(float threshold) {
        this.threshold = threshold;
    }
}
