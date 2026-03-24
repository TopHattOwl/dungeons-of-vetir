package com.tophattowl.dungeonsofvetir.game.combat.calculators;

import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.combat.context.AttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.AttackResult;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class MeleeAttackCalculator implements AttackCalculator {
    @Override
    public AttackResult calculate(AttackContext context, GameWorld gameWorld) {
        return null;
    }

    @Override
    public AttackType getType() {
        return AttackType.MELEE;
    }
}
