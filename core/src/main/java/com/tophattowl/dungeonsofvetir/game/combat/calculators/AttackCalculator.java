package com.tophattowl.dungeonsofvetir.game.combat.calculators;

import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.combat.context.AttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.AttackResult;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.List;

public interface AttackCalculator<T extends AttackResult, V extends AttackContext> {
    List<T> calculate(V context, GameWorld gameWorld);
    AttackType getType();
}
