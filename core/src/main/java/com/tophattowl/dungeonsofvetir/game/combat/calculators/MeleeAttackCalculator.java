package com.tophattowl.dungeonsofvetir.game.combat.calculators;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.monsters.NaturalWeapon;
import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageInstance;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.*;

public class MeleeAttackCalculator implements AttackCalculator<MeleeAttackResult, MeleeAttackContext> {
    private final Random rng = new Random(System.currentTimeMillis());

    @Override
    public List<MeleeAttackResult> calculate(MeleeAttackContext context, GameWorld gameWorld) {
        Entity attacker = context.getAttacker();

        if (!attacker.hasComponent(EquipmentComponent.class)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Calculating with no equipment component"
            );
            return calculateNoEquipment(context, gameWorld);
        }

        EquipmentComponent attackerEquipment = attacker.getComponent(EquipmentComponent.class);

        Item mainHandItem = attackerEquipment.getMainHandSlot().item;
        Item offHandItem = attackerEquipment.getOffHandSlot().item;

        if (mainHandItem == null) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Calculating unarmed"
            );
            return calculateUnarmed(context, gameWorld);
        }

        if (mainHandItem.equals(offHandItem)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Calculating two handed"
            );
            return calculateTwoHanded(context, gameWorld, mainHandItem);
        } else {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Calculating one handed"
            );
            return calculateOneHanded(context, gameWorld, mainHandItem, offHandItem);
        }
    }

    private List<MeleeAttackResult> calculateTwoHanded(MeleeAttackContext context,GameWorld gameWorld,
                                                       Item twoHandedItem) {
        MeleeAttackResult result = new MeleeAttackResult();

        Entity attacker = context.getAttacker();
        Entity target = context.getTarget();

        OffensiveStatsComponent attackerOffense = attacker.getComponent(OffensiveStatsComponent.class);

        DefensiveStatsComponent targetDefense = target.getComponent(DefensiveStatsComponent.class);
        BodyComponent targetBody = target.getComponent(BodyComponent.class);

        MeleeWeaponComponent weaponComp = twoHandedItem.getComponent(MeleeWeaponComponent.class);

        BodyPart targetedBodyPart = targetBody.getRandomBodyPart();
        result.setBodyPart(targetedBodyPart);

        // acc check
        if (isMissed(attackerOffense.accuracy, targetDefense.evasion)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Attack missed"
            );
            result.missed();
            return List.of(result);
        }

        // block check
        if (isBlocked(targetDefense.blockChance)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Attack blocked"
            );
            result.blocked();
            return List.of(result);
        }

        // attack calc
        for (Map.Entry<ElementType, DamageInstance> entry : weaponComp.getDamages().entrySet()) {
            DamageInstance damage = entry.getValue();
            DamageType damageType = weaponComp.getDamageType();
            ElementType element = entry.getKey();

            result.addDamage(new Damage(damage.getBaseAmount(), element, damageType));
        }

        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
            "damages added to result"
        );

        // counter check
        if (isCountered(targetDefense.counterChance)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "attack countered"
            );
            result.countered();
        }
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
            "final attack result: " + result.toString()
        );
        return List.of(result);
    }

    private List<MeleeAttackResult> calculateOneHanded(MeleeAttackContext context, GameWorld gameWorld,
                                                       Item mainHandItem, Item offHandItem) {
        // only main hand for now for attack
        // TODO: handle offhand, dual wield with two attacks if both are weapons
        MeleeAttackResult result = new MeleeAttackResult();

        Entity attacker = context.getAttacker();
        Entity target = context.getTarget();

        OffensiveStatsComponent attackerOffense = attacker.getComponent(OffensiveStatsComponent.class);

        DefensiveStatsComponent targetDefense = target.getComponent(DefensiveStatsComponent.class);
        BodyComponent targetBody = target.getComponent(BodyComponent.class);

        MeleeWeaponComponent weaponComp = mainHandItem.getComponent(MeleeWeaponComponent.class);

        BodyPart targetedBodyPart = targetBody.getRandomBodyPart();
        result.setBodyPart(targetedBodyPart);

        // acc check
        if (isMissed(attackerOffense.accuracy, targetDefense.evasion)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Attack missed"
            );
            result.missed();
            return List.of(result);
        }

        // block check
        if (isBlocked(targetDefense.blockChance)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Attack blocked"
            );
            result.blocked();
            return List.of(result);
        }

        // attack calc
        for (Map.Entry<ElementType, DamageInstance> entry : weaponComp.getDamages().entrySet()) {
            DamageInstance damage = entry.getValue();
            DamageType damageType = weaponComp.getDamageType();
            ElementType element = entry.getKey();

            result.addDamage(new Damage(damage.getBaseAmount(), element, damageType));
        }

        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
            "damages added to result"
        );

        // counter check
        if (isCountered(targetDefense.counterChance)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "attack countered"
            );
            result.countered();
        }
        DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
            "final attack result: " + result.toString()
        );
        return List.of(result);
    }

    private List<MeleeAttackResult> calculateUnarmed(MeleeAttackContext context, GameWorld gameWorld) {

        return List.of();
    }

    private List<MeleeAttackResult> calculateNoEquipment(MeleeAttackContext context, GameWorld gameWorld) {

        List<MeleeAttackResult> results = new ArrayList<>();

        Entity attacker = context.getAttacker();
        Entity target = context.getTarget();

        NaturalWeaponsComponent naturalWeaponsComp = attacker.getComponent(NaturalWeaponsComponent.class);
        OffensiveStatsComponent attackerOffense = attacker.getComponent(OffensiveStatsComponent.class);

        DefensiveStatsComponent targetDefense = target.getComponent(DefensiveStatsComponent.class);
        BodyComponent targetBody = target.getComponent(BodyComponent.class);

        for (NaturalWeapon natWeapon : naturalWeaponsComp.getNaturalWeaponsSorted()) {
            float attackChance = natWeapon.getAttackChance();

            if (attackChance < rng.nextFloat()) continue;

            MeleeAttackResult result = new MeleeAttackResult();
            BodyPart targetedBodyPart = targetBody.getRandomBodyPart();
            result.setBodyPart(targetedBodyPart);

            // acc check
            if (isMissed(attackerOffense.accuracy, targetDefense.evasion)) {
                result.missed();
                results.add(result);
                continue;
            }

            // block check
            if (isBlocked(targetDefense.blockChance)) {
                result.blocked();
                results.add(result);
                continue;
            }

            // attack calc
            for (Map.Entry<ElementType, DamageInstance> entry : natWeapon.getDamages().entrySet()) {
                DamageInstance damage = entry.getValue();
                DamageType damageType = natWeapon.getDamageType();
                ElementType element = entry.getKey();

                result.addDamage(new Damage(damage.getBaseAmount(), element, damageType));
            }

            // counter
            if (isCountered(targetDefense.counterChance)) {
                result.countered();
            }

            results.add(result);
        }

        return results;
    }


    private boolean isMissed(int attackerAcc, int targetEvasion) {
        int roll = rng.nextInt( attackerAcc + targetEvasion);
        return roll >= attackerAcc;
    }

    private boolean isBlocked(float blockChance) {
        return rng.nextFloat() < blockChance;
    }

    private boolean isCountered(float counterChance) {
        return rng.nextFloat() < counterChance;
    }

    @Override
    public AttackType getType() {
        return AttackType.MELEE;
    }
}
