package com.tophattowl.dungeonsofvetir.game.stats;

/**
 * Whether a stat is a flat (integer-like) value or a percent
 * <p>
 * Percent stats are stored as fractions ({@code 0.10f} == 10%), so additive percentage
 * growth stays simple: {@code 50% + 1.5% = 51.5%}.
 */
public enum ValueType {
    FLAT,
    PERCENT,
}
