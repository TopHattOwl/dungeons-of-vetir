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
import com.tophattowl.dungeonsofvetir.game.stats.CombatStat;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.*;

/**
 * Rolls a melee attack into one or more {@link MeleeAttackResult}s <br>
 * Reads derived stats for hit/dodge/block/counter and unarmed damage; <br>
 * damage mitigation (protection, resistance, block power, crit, procs) not implemented yet
 */
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
        Item mainHandItem = attackerEquipment.getMainHandSlot() == null ? null
            : attackerEquipment.getMainHandSlot().item;
        Item offHandItem = attackerEquipment.getOffHandSlot() == null ? null
            : attackerEquipment.getOffHandSlot().item;

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

        int baseDamage = offenseOf(attacker).getInt(CombatStat.MELEE_DAMAGE);
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

        DerivedStatsComponent attackerOffense = attacker.getComponent(DerivedStatsComponent.class);
        DerivedStatsComponent targetDefense = target.getComponent(DerivedStatsComponent.class);

        float accuracy = attackerOffense == null ? 1f : attackerOffense.get(CombatStat.ACCURACY);
        float dodge = targetDefense == null ? 0f : targetDefense.get(CombatStat.DODGE_CHANCE);
        float block = targetDefense == null ? 0f : targetDefense.get(CombatStat.BLOCK_CHANCE);
        float counter = targetDefense == null ? 0f : targetDefense.get(CombatStat.COUNTER_CHANCE);

        // miss check
        if (isMissed(accuracy)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator", "Attack missed");
            result.missed();
            return result;
        }

        // dodge check
        if (isDodged(dodge, accuracy)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator", "Attack dodged");
            result.missed();
            return result;
        }

        // block check
        if (isBlocked(block)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator", "Attack blocked");
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
        if (isCountered(counter)) {
            DebugLogger.log(DebugLogger.Category.COMBAT, "MeleeAttackCalculator", "attack countered");
            result.countered();
        }

        return result;
    }

    private DerivedStatsComponent offenseOf(Entity entity) {
        DerivedStatsComponent derived = entity.getComponent(DerivedStatsComponent.class);
        if (derived == null) {
            throw new IllegalStateException("Attacker has no derived stats:\n" + entity);
        }
        return derived;
    }

    /**
     * Accuracy is a chance (1.0 == 100%). Below 100% it can miss; at or above 100% it connects.
     */
    private boolean isMissed(float accuracy) {
        if (accuracy >= 1f) return false;
        return rng.nextFloat() >= accuracy;
    }

    /**
     * Dodge is a chance, reduced multiplicatively by accuracy above 100%.
     */
    private boolean isDodged(float dodge, float accuracy) {
        float excess = Math.max(0f, accuracy - 1f);
        float effective = Math.max(0f, Math.min(dodge * (1f - excess), 0.95f));
        return rng.nextFloat() < effective;
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
