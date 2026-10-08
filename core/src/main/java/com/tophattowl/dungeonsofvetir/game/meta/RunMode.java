package com.tophattowl.dungeonsofvetir.game.meta;

/**
 * What a run starts as. The first run is a dungeon; once the overworld is unlocked
 * (meta), later runs begin there.
 */
public enum RunMode {
    FIRST_DUNGEON,
    OVERWORLD,
    ;

    public static RunMode forMeta(MetaState meta) {
        return meta.isOverworldUnlocked() ? OVERWORLD : FIRST_DUNGEON;
    }
}
