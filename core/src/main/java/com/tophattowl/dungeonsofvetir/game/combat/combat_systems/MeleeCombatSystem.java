package com.tophattowl.dungeonsofvetir.game.combat.combat_systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.MeleeAttackAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.OffensiveStatsComponent;
import com.tophattowl.dungeonsofvetir.game.combat.calculators.CombatCalculators;
import com.tophattowl.dungeonsofvetir.game.combat.calculators.MeleeAttackCalculator;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.List;

public class MeleeCombatSystem implements GameSystem {


    public static Action prepareMeleeAttack(MeleeAttackAction meleeAttackAction, GameWorld gameWorld) {
        meleeAttackAction.possible();
        return meleeAttackAction;
    }

    public static Action executeMeleeAttack(MeleeAttackAction meleeAttackAction, GameWorld gameWorld) {
        Entity attacker = meleeAttackAction.getOwner();
        Entity target = meleeAttackAction.getTarget();

        MeleeAttackContext context = new MeleeAttackContext(attacker, target);
        List<MeleeAttackResult> attackResults = CombatCalculators.getCalculator(MeleeAttackCalculator.class)
            .calculate(context, gameWorld);

        for (MeleeAttackResult attack : attackResults) {
            List<Damage> damages = attack.getDamages();
            BodyPart targetPart = attack.getBodyPart();
            HealthComponent targetHp = target.getComponent(HealthComponent.class);

            for (Damage damage : damages) {
                if (targetHp.takeDamage(damage.amount())) {
                    die(target, gameWorld);
                }
                int partDamage = (int) (damage.amount() * targetPart.damageMultiplier);
                targetPart.takeDamage(partDamage);
            }
        }

        meleeAttackAction.success();

        return meleeAttackAction;
    }

    public static void die(Entity entity, GameWorld gameWorld) {
        gameWorld.removeEntity(entity);
    }
}
