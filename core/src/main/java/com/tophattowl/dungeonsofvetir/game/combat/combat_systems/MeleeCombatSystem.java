package com.tophattowl.dungeonsofvetir.game.combat.combat_systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.MeleeAttackAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.OffensiveStatsComponent;
import com.tophattowl.dungeonsofvetir.game.combat.calculators.CombatCalculators;
import com.tophattowl.dungeonsofvetir.game.combat.calculators.MeleeAttackCalculator;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class MeleeCombatSystem implements GameSystem {


    public static Action prepareMeleeAttack(MeleeAttackAction meleeAttackAction, GameWorld gameWorld) {
        meleeAttackAction.possible();
        return meleeAttackAction;
    }

    public static Action executeMeleeAttack(MeleeAttackAction meleeAttackAction, GameWorld gameWorld) {
        Entity attacker = meleeAttackAction.getOwner();
        Entity target = meleeAttackAction.getTarget();
        OffensiveStatsComponent attackerOffensiveComp = attacker.getComponent(OffensiveStatsComponent.class);
        HealthComponent targetHealthComp = target.getComponent(HealthComponent.class);


        MeleeAttackContext context = new MeleeAttackContext(attacker, target);
        List<MeleeAttackResult> attackResults = CombatCalculators.getCalculator(MeleeAttackCalculator.class)
            .calculate(context, gameWorld);

        // execute based on attackResult

        // OLD
//        BodyPart targetedBodyPart = attackResult.getBodyPart();
//        int damage = attackerOffensiveComp.baseDamage / 2;
//        float damageMulti = targetedBodyPart.damageMultiplier;
//
//        // apply damage to body part
//        if (targetedBodyPart.applyDamage(damage) && targetedBodyPart.isVital()) {
//            die(target, gameWorld);
//        }
//
//        // apply damage to health comp
//        if (targetHealthComp.takeDamage((int) (damage * damageMulti))) {
//            die(target, gameWorld);
//        }
//
//        DebugLogger.log(DebugLogger.Category.COMBAT, "CombatSystem",
//            "Random rolled bodypart: \n" + targetedBodyPart
//        );
//
//        meleeAttackAction.success();
        // OLD END


        return meleeAttackAction;
    }


    public static void die(Entity entity, GameWorld gameWorld) {
        gameWorld.removeEntity(entity);
    }
}
