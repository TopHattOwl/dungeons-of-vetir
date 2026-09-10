package com.tophattowl.dungeonsofvetir.game.dungeon.generators;

/**
 * Tunables for the cellular-automata cave generator. Variations of the cave
 * section can supply different params to get a different feel
 */
public record CaveParams(
    double fillChance,
    int smoothPasses,
    int wallThreshold,
    int floorVariants
) {
    public static final CaveParams DEFAULT = new CaveParams(0.48, 5, 5, 4);
}
