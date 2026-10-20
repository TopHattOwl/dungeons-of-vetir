package com.tophattowl.dungeonsofvetir.game.status;

/**
 * How re-applying an already-present effect behaves.
 */
public enum StackingPolicy {
    /** Reset the duration, keep the stack count. */
    REFRESH,
    /** Add a stack (stronger) and refresh the duration. */
    INTENSITY,
    /** Add a separate, independent instance. */
    INDEPENDENT,
}
