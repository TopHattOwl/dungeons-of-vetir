package com.tophattowl.dungeonsofvetir.game.world;

/**
 * What kind of opening a level transition is. Purely descriptive of the link; the
 * mechanism (enter the target place, spawn on the matching arrival tile) is the same
 */
public enum TransitionKind {
    STAIRS_UP,
    STAIRS_DOWN,
    DOOR,
    GATE,
    HATCH,
}
