package com.tophattowl.dungeonsofvetir.game.combat.context;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.combat.AttackType;

/**
 * Input for AttackCalculators
 */
public abstract class AttackContext {
    protected Entity attacker;
    protected Entity target;
    protected AttackType attackType;

    public AttackContext(Entity attacker, Entity target, AttackType attackType) {
        this.attacker = attacker;
        this.target = target;
        this.attackType = attackType;
    }

    public Entity getAttacker() {
        return attacker;
    }

    public Entity getTarget() {
        return target;
    }

    public AttackType getAttackType() {
        return attackType;
    }
}
