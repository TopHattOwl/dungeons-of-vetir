package com.tophattowl.dungeonsofvetir.game.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.AttackAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.OffensiveStatsComponent;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.ActionCompletedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class CombatSystem implements GameSystem {

    public Action tryAttack(AttackAction attackAction, GameWorld gameWorld) {
        Entity attacker = attackAction.getOwner();
        Entity target = attackAction.getTarget();
        OffensiveStatsComponent attackerOffensiveComp = attacker.getComponent(OffensiveStatsComponent.class);

        HealthComponent targetHealthComp = target.getComponent(HealthComponent.class);

        // body part based damage calc
        BodyComponent targetBodyComp = target.getComponent(BodyComponent.class);
        BodyPart targetedBodyPart = targetBodyComp.getRandomBodyPart();
        int damage = attackerOffensiveComp.baseDamage / 2;
        float damageMulti = targetedBodyPart.damageMultiplier;

        // apply damage to body part
        if (targetedBodyPart.applyDamage(damage) && targetedBodyPart.isVital()) {
            die(target, gameWorld);
        }

        // apply damage to health comp
        if (targetHealthComp.takeDamage((int) (damage * damageMulti))) {
            die(target, gameWorld);
        }

        DebugLogger.log(DebugLogger.Category.COMBAT, "CombatSystem",
            "Random rolled bodypart: \n" + targetedBodyPart
        );

        attackAction.success();
        EventBus.emit(new ActionCompletedEvent(attacker, attackAction));

        return attackAction;
    }


    public void die(Entity entity, GameWorld gameWorld) {
        gameWorld.removeEntity(entity);
    }
}
