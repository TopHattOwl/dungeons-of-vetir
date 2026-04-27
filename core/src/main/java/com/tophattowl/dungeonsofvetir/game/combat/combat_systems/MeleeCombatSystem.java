package com.tophattowl.dungeonsofvetir.game.combat.combat_systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.MeleeAttackAction;
import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.combat.calculators.CombatCalculators;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.combat.*;
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
        List<MeleeAttackResult> attackResults = CombatCalculators.getMeleeCalculator().calculate(context, gameWorld);

        for (MeleeAttackResult attack : attackResults) {
            if (attack.isMissed()) {
                applyMissed(attack, attacker, target, gameWorld);
                continue;
            }
            if (attack.isBlocked()) {
                applyBlocked(attack, attacker, target, gameWorld);
                continue;
            }
            applyAttack(attack, attacker, target, gameWorld);
        }

        meleeAttackAction.success();

        return meleeAttackAction;
    }

    private static void applyAttack(MeleeAttackResult attackResult,
                                    Entity attacker, Entity target,
                                    GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeCombatSystem",
            "applying attack");

        List<Damage> damages = attackResult.getDamages();
        BodyPart targetPart = attackResult.getBodyPart();
        HealthComponent targetHp = target.getComponent(HealthComponent.class);

        for(Damage damage : damages) {
            if (targetHp.takeDamage(damage.amount())) {
                die(target, gameWorld, attacker);
            }
            int partDamage = (int) (damage.amount() * targetPart.damageMultiplier);
            targetPart.takeDamage(partDamage);
        }
        EventBus.emit(new MeleeAttackHitEvent(attacker, target, targetPart, damages, attackResult.getUsedWeapon()));

        if (attackResult.isCountered()) {
            applyCountered(attackResult, attacker, target, gameWorld);
        }
    }

    private static void applyMissed(MeleeAttackResult attackResult,
                                    Entity attacker, Entity target,
                                    GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeCombatSystem",
            "applying missed attack");
        EventBus.emit(new MeleeAttackMissedEvent(attacker, target));
    }

    private static void applyBlocked(MeleeAttackResult attackResult,
                                    Entity attacker, Entity target,
                                    GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeCombatSystem",
            "applying blocked attack");
        EventBus.emit(new MeleeAttackBlockedEvent(attacker, target));
    }

    private static void applyCountered(MeleeAttackResult attackResult,
                                       Entity attacker, Entity target,
                                       GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeCombatSystem",
            "applying counter after attack");
        EventBus.emit(new MeleeAttackCounteredEvent(target, attacker));
    }

    public static void die(Entity entity, GameWorld gameWorld, Entity killer) {
        gameWorld.removeEntity(entity);
        EventBus.emit(new EntityKilledEvent(entity, killer));
    }
}
