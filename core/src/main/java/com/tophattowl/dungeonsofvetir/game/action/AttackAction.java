package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.combat.CombatSystem;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class AttackAction extends Action {
    private Entity target;

    public AttackAction(Entity owner, Entity target) {
        super(ActionType.ATTACK, owner);
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }


    @Override
    public Action prepare(GameWorld gameWorld) {
        return gameWorld.getSystem(CombatSystem.class).prepareAttack(this,  gameWorld);
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return gameWorld.getSystem(CombatSystem.class).executeAttack(this, gameWorld);
    }

    @Override
    public String toString() {
        return "[AttackAction]: " +
            "target=" + target +
            ", cost=" + cost +
            ", owner=" + owner;
    }
}
