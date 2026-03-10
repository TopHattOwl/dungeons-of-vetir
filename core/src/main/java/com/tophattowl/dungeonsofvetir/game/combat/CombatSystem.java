package com.tophattowl.dungeonsofvetir.game.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.AttackAction;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class CombatSystem implements GameSystem {

    public Action tryAttack(AttackAction attackAction, GameWorld gameWorld) {
        Entity attacker = attackAction.getOwner();
        Entity target = attackAction.getTarget();

        HealthComponent targetHealthComp = target.getComponent(HealthComponent.class);

        targetHealthComp.hp -= 5;
        if (targetHealthComp.hp <= 0) {
            die(target, gameWorld);
        }

        attackAction.success();
        System.out.println("entity attacked, hp: " + targetHealthComp.hp);

        return attackAction;
    }


    public void die(Entity entity, GameWorld gameWorld) {
        gameWorld.removeEntity(entity);
    }
}
