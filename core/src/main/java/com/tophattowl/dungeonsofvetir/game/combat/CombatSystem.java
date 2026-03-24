package com.tophattowl.dungeonsofvetir.game.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.MeleeAttackAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.OffensiveStatsComponent;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class CombatSystem implements GameSystem {


    public Action prepareMeleeAttack(MeleeAttackAction meleeAttackAction, GameWorld gameWorld) {
        meleeAttackAction.possible();
        return meleeAttackAction;
    }

    public Action executeMeleeAttack(MeleeAttackAction meleeAttackAction, GameWorld gameWorld) {
        Entity attacker = meleeAttackAction.getOwner();
        Entity target = meleeAttackAction.getTarget();
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

        meleeAttackAction.success();

        return meleeAttackAction;
    }


    public void die(Entity entity, GameWorld gameWorld) {
        gameWorld.removeEntity(entity);
    }
}
