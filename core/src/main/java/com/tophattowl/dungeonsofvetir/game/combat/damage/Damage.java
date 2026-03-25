package com.tophattowl.dungeonsofvetir.game.combat.damage;


import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;

/**
 * record for applying damage
 */
public record Damage(
    int amount,
    ElementType elementType,
    DamageType damageType
) {
}
