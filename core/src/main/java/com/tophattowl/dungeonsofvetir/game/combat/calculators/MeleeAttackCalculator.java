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
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageProfile;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;
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
            return calculateNoEquipment(context);
        }

        EquipmentComponent attackerEquipment = attacker.getComponent(EquipmentComponent.class);
        Item mainHandItem = attackerEquipment.getMainHandSlot().item;
        Item offHandItem = attackerEquipment.getOffHandSlot().item;

        if (mainHandItem == null || !mainHandItem.hasComponent(MeleeWeaponComponent.class)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Calculating unarmed"
            );
            return calculateUnarmed(context);
        }

        MeleeWeaponComponent mainWeaponComp = mainHandItem.getComponent(MeleeWeaponComponent.class);

        if (mainWeaponComp.getGripType() == ItemGripType.TWO_HANDED) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Calculating two handed"
            );
            return List.of(buildWeaponResult(context, mainWeaponComp, mainHandItem));

        }

        if (offHandItem != null && offHandItem.hasComponent(MeleeWeaponComponent.class)){
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Calculating dual wield"
            );
            return calculateDualWield(context, mainHandItem, offHandItem);

        }

        return List.of(buildWeaponResult(context, mainWeaponComp, mainHandItem));
    }

    private List<MeleeAttackResult> calculateDualWield(MeleeAttackContext context,
                                                       Item mainHandItem, Item offHandItem) {
        MeleeWeaponComponent mainWeaponComp = mainHandItem.getComponent(MeleeWeaponComponent.class);
        MeleeWeaponComponent offWeaponComp = offHandItem.getComponent(MeleeWeaponComponent.class);

        MeleeAttackResult mainResult = buildWeaponResult(context, mainWeaponComp, mainHandItem);
        MeleeAttackResult offHandResult = buildWeaponResult(context, offWeaponComp, offHandItem);

        return List.of(mainResult, offHandResult);
    }

    private List<MeleeAttackResult> calculateUnarmed(MeleeAttackContext context) {
        Entity attacker = context.getAttacker();
        OffensiveStatsComponent offensiveComp = attacker.getComponent(OffensiveStatsComponent.class);

        int baseDamage = offensiveComp.baseDamage;
        // placeholder anon class for fists
        DamageProfile fist = new DamageProfile() {

            @Override
            public Map<ElementType, DamageInstance> getDamages() {
                return Map.of(ElementType.PHYSICAL, new DamageInstance(baseDamage, ElementType.PHYSICAL));
            }

            @Override
            public DamageType getDamageType() {
                return DamageType.CRUSHING;
            }
        };

        return List.of(buildWeaponResult(context, fist, null));


    }

    private List<MeleeAttackResult> calculateNoEquipment(MeleeAttackContext context) {
        List<MeleeAttackResult> results = new ArrayList<>();

        Entity attacker = context.getAttacker();

        if (!attacker.hasComponent(NaturalWeaponsComponent.class)) {
            throw new IllegalStateException("Attacker has no Equipment or Natural weapons component:\n" + attacker);
        }

        NaturalWeaponsComponent naturalWeaponsComp = attacker.getComponent(NaturalWeaponsComponent.class);

        for (NaturalWeapon weapon : naturalWeaponsComp.getNaturalWeaponsSorted()) {
            float attackChance = weapon.getAttackChance();
            if (attackChance < rng.nextFloat()) continue;

            results.add(buildWeaponResult(context, weapon, null));
        }

        return results;
    }


    private MeleeAttackResult buildWeaponResult(MeleeAttackContext context, DamageProfile weapon, Item usedItem) {
        Entity attacker = context.getAttacker();
        Entity target = context.getTarget();

        MeleeAttackResult result = new MeleeAttackResult();
        result.setUsedWeapon(usedItem);

        BodyComponent targetBody = target.getComponent(BodyComponent.class);
        result.setBodyPart(targetBody.getRandomBodyPart());

        OffensiveStatsComponent attackerOffense = attacker.getComponent(OffensiveStatsComponent.class);
        DefensiveStatsComponent targetDefense = target.getComponent(DefensiveStatsComponent.class);

        // acc check
        if (isMissed(attackerOffense.accuracy, targetDefense.evasion)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Attack missed"
            );
            result.missed();
            return result;
        }

        // block check
        if (isBlocked(targetDefense.blockChance)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "Attack blocked"
            );
            result.blocked();
            return result;
        }

        // attack calc
        for (Map.Entry<ElementType, DamageInstance> entry : weapon.getDamages().entrySet()) {
            DamageInstance damage = entry.getValue();
            DamageType damageType = weapon.getDamageType();
            ElementType element = entry.getKey();

            result.addDamage(new Damage(damage.getBaseAmount(), element, damageType));
        }

        // counter check
        if (isCountered(targetDefense.counterChance)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator",
                "attack countered"
            );
            result.countered();
        }

        return result;
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
