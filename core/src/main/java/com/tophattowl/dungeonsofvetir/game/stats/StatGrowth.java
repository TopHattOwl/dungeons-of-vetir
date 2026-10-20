package com.tophattowl.dungeonsofvetir.game.stats;

/**
 * How a {@link CombatStat} is derived from its governing attribute:
 *
 * <pre>
 * value = base + perPoint * attribute + perFive * floor(attribute / 5)
 * </pre>
 *
 * then modifiers are applied and the result is clamped to {@code cap} (if > 0).
 * <p>
 * A null {@link #owner} marks a modifier-only stat: it is never raised by an attribute, only
 * by gear/conditions/expertise. Percent values ({@link ValueType#PERCENT}) are fractions
 */
public record StatGrowth(
    CombatStat stat,
    Attribute owner,
    ValueType type,
    float base,
    float perPoint,
    float perFive,
    float cap
) {

    public boolean isModifierOnly() {
        return owner == null;
    }

    public boolean hasCap() {
        return cap > 0f;
    }
}
