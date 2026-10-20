package com.tophattowl.dungeonsofvetir.game.stats;

/**
 * The operation a {@link Modifier} applies to its target stat.
 * <p>
 * All modifiers from all sources are gathered, then applied <b>globally</b> in this order:
 * <ol>
 *     <li>{@code FLAT} (sum)</li>
 *     <li>{@code PERCENT} (add)</li>
 *     <li>{@code MULTIPLY} the product</li>
 * </ol>
 */
public enum ModifierOperation {
    FLAT,
    PERCENT,
    MULTIPLY,
}
