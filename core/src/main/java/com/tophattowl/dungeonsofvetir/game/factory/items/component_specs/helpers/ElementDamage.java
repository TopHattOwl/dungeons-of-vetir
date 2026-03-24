package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.helpers;

import com.tophattowl.dungeonsofvetir.game.combat.ElementType;

/**
 * Helper record for melee weapon component
 */
public record ElementDamage(ElementType elementType, int amount){}
