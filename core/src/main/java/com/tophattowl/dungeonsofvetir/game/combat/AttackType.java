package com.tophattowl.dungeonsofvetir.game.combat;


/**
 * TBD: ABILITY type maybe could be dropped, since spell is ability and that has its own type,
 * abilities that use melee or ranged attack will just be of the MELEE or RANGED type
 */
public enum AttackType {
    MELEE,
    RANGED,
    SPELL,
    ABILITY,
}
