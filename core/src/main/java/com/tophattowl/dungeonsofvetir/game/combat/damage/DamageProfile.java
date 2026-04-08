package com.tophattowl.dungeonsofvetir.game.combat.damage;

import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;

import java.util.Map;

public interface DamageProfile {
    Map<ElementType, DamageInstance> getDamages();
    DamageType getDamageType();
}
