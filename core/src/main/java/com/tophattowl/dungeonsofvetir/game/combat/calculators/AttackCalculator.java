package com.tophattowl.dungeonsofvetir.game.combat.calculators;

import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.combat.context.AttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.AttackResult;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public interface AttackCalculator {
    AttackResult calculate(AttackContext context, GameWorld gameWorld);
    AttackType getType();
}
