package com.tophattowl.dungeonsofvetir.game.world;

/**
 * Something an entity may or may not do. Denied by one or more sources (current
 * zone, timed effects, injuries). Queried via {@link ConstraintSystem#can}.
 */
public enum Capability {
    MOVE,
    ATTACK,
    EQUIP,
    REST,
    CAST,
    USE_ITEM,
    VISION,
}
