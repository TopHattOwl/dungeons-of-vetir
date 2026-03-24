package com.tophattowl.dungeonsofvetir.game.combat.context;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.combat.AttackType;

public class MeleeAttackContext extends AttackContext{

    public MeleeAttackContext(Entity attacker, Entity target) {
        super(attacker, target, AttackType.MELEE);
    }
}
