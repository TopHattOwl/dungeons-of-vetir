package com.tophattowl.dungeonsofvetir.game.world;

/**
 * A level transition on a tile: using it enters {@code target}, arriving on the
 * matching tile there. Entries and exits are reciprocal (a stairs-down on floor N
 * points to floor N+1, whose stairs-up points back).
 */
public record Exit(Point tile, TransitionKind kind, Place target) {
}
