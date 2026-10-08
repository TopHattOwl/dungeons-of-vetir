package com.tophattowl.dungeonsofvetir.game.dungeon;

/**
 * Per-run state for a dungeon: its seed and whether it has been cleared. The seam
 * for refill/clear/dynamic behaviour later; nothing consumes the flag yet.
 */
public class DungeonState {

    private final String key;
    private final long seed;
    private boolean cleared;

    public DungeonState(String key, long seed) {
        this.key = key;
        this.seed = seed;
    }

    public String key() {
        return key;
    }

    public long seed() {
        return seed;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void markCleared() {
        this.cleared = true;
    }

    public void reset() {
        this.cleared = false;
    }
}
