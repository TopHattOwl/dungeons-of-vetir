package com.tophattowl.dungeonsofvetir.game.status;

import java.util.List;

/**
 * A repeating set of actions fired every {@code intervalTurns} world turns while the effect is
 * active. Multiple periodic effects on one definition may run at different intervals.
 */
public record PeriodicEffect(int intervalTurns, List<EffectAction> actions) {

    public PeriodicEffect {
        if (intervalTurns < 1) {
            throw new IllegalArgumentException("periodic interval must be >= 1");
        }
    }
}
