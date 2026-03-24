package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.combat.CombatSystem;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class MeleeAttackAction extends Action {
    private final Entity target;

    public MeleeAttackAction(Entity owner, Entity target) {
        super(ActionType.MELEE_ATTACK, owner);
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }


    @Override
    public Action prepare(GameWorld gameWorld) {
        return gameWorld.getSystem(CombatSystem.class).prepareMeleeAttack(this,  gameWorld);
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return gameWorld.getSystem(CombatSystem.class).executeMeleeAttack(this, gameWorld);
    }

    @Override
    public String toString() {
        return "[AttackAction]: " +
            "target=" + target +
            ", cost=" + cost +
            ", owner=" + owner;
    }
}
