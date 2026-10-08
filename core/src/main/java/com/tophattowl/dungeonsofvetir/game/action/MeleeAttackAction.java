package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.action.resolvers.MeleeResolver;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class MeleeAttackAction extends Action {
    private final Entity target;
    private final AttackType attackType;

    public MeleeAttackAction(Entity owner, Entity target) {
        super(ActionType.MELEE_ATTACK, owner);
        this.target = target;
        this.attackType = AttackType.MELEE;
    }

    public Entity getTarget() { return target; }
    public AttackType getAttackType() { return attackType; }

    @Override
    public Action prepare(GameWorld gameWorld) {
        if (!gameWorld.constraints().can(owner, Capability.ATTACK)) return this;
        return MeleeResolver.prepareMeleeAttack(this,  gameWorld);
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return MeleeResolver.executeMeleeAttack(this, gameWorld);
    }

    @Override
    public String toString() {
        return "[AttackAction]: " +
            "target=" + target +
            ", cost=" + cost +
            ", owner=" + owner;
    }
}
