package com.tophattowl.dungeonsofvetir.game.action.resolvers;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.MeleeAttackAction;
import com.tophattowl.dungeonsofvetir.game.combat.calculators.CombatCalculators;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.combat.*;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.List;

/**
 * Resolves a {@link MeleeAttackAction} on demand
 */
public final class MeleeResolver {

    private MeleeResolver() {}

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

    /**
     * Resolves the whole hit before checking death, so a dead target is not damaged
     * again and cannot counter.
     * <p>
     * Condition damage is intentionally scaled twice: a per-part multiplier (heads
     * are frailer than limbs) applied here, and the global BodyPart condition scale
     * applied inside {@link BodyPart#takeDamage(int)}. Actor HP takes full damage.
     */
    private static void applyAttack(MeleeAttackResult attackResult,
                                    Entity attacker, Entity target,
                                    GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeResolver",
            "applying attack");

        List<Damage> damages = attackResult.getDamages();
        BodyPart targetPart = attackResult.getBodyPart();
        HealthComponent targetHp = target.getComponent(HealthComponent.class);

        int totalHpDamage = 0;
        int totalPartDamage = 0;
        for (Damage damage : damages) {
            totalHpDamage += damage.amount();
            totalPartDamage += (int) (damage.amount() * targetPart.damageMultiplier);
        }

        boolean hpDepleted = targetHp.takeDamage(totalHpDamage);
        targetPart.takeDamage(totalPartDamage);

        EventBus.emit(new MeleeAttackHitEvent(attacker, target, targetPart, damages, attackResult.getUsedWeapon()));

        boolean vitalPartDestroyed = targetPart.isDestroyed() && targetPart.isVital();

        if (hpDepleted || vitalPartDestroyed) {
            die(target, gameWorld, attacker);
            return; // dead men tell no tails (and do not counter either)
        }

        if (attackResult.isCountered()) {
            applyCountered(attackResult, attacker, target, gameWorld);
        }
    }

    private static void applyMissed(MeleeAttackResult attackResult,
                                    Entity attacker, Entity target,
                                    GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeResolver",
            "applying missed attack");
        EventBus.emit(new MeleeAttackMissedEvent(attacker, target));
    }

    private static void applyBlocked(MeleeAttackResult attackResult,
                                    Entity attacker, Entity target,
                                    GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeResolver",
            "applying blocked attack");
        EventBus.emit(new MeleeAttackBlockedEvent(attacker, target));
    }

    private static void applyCountered(MeleeAttackResult attackResult,
                                       Entity attacker, Entity target,
                                       GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeResolver",
            "applying counter after attack");
        EventBus.emit(new MeleeAttackCounteredEvent(target, attacker));
    }

    public static void die(Entity entity, GameWorld gameWorld, Entity killer) {
        gameWorld.removeEntity(entity);
        EventBus.emit(new EntityKilledEvent(entity, killer));
    }
}
