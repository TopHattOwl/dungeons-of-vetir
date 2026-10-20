package com.tophattowl.dungeonsofvetir.game.stats;

/**
 * A single change to a stat, contributed by a {@link ModifierSource}
 * (innate, gear, status, expertise, stance) <br>
 *
 * <b>Immutable </b> <br>
 * sources are re-collected on every aggregation
 *
 * @param target    the stat this modifies
 * @param operation how it applies (see {@link ModifierOperation})
 * @param value     the amount, percent values are fractions (0.1 = 10%)
 */
public record Modifier(StatRef target, ModifierOperation operation, float value) {

    public static Modifier flat(StatRef target, float value) {
        return new Modifier(target, ModifierOperation.FLAT, value);
    }

    public static Modifier percent(StatRef target, float value) {
        return new Modifier(target, ModifierOperation.PERCENT, value);
    }

    public static Modifier multiply(StatRef target, float value) {
        return new Modifier(target, ModifierOperation.MULTIPLY, value);
    }
}
