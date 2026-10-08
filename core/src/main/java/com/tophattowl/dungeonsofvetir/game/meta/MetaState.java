package com.tophattowl.dungeonsofvetir.game.meta;

/**
 * Persistent-across-runs progression state. In-memory only for now; real save/load
 * is a later phase.
 */
public class MetaState {

    private boolean overworldUnlocked = false;

    public boolean isOverworldUnlocked() {
        return overworldUnlocked;
    }

    public void unlockOverworld() {
        this.overworldUnlocked = true;
    }
}
