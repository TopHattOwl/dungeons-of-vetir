package com.tophattowl.dungeonsofvetir.game.stats;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * The single source of truth for how every {@link CombatStat} grows from attributes.
 * <p>
 * Magnitudes here are first-pass placeholders and are tuned in playtesting (see
 * {@code docs/DESIGN.md} §5 and {@code docs/IMPLEMENTATION.md} phase 3A).
 */
public final class StatCatalog {

    private static final Map<CombatStat, StatGrowth> GROWTH = new EnumMap<>(CombatStat.class);

    private StatCatalog() {}

    public static StatGrowth get(CombatStat stat) {
        StatGrowth growth = GROWTH.get(stat);
        if (growth == null) {
            throw new IllegalStateException("No StatGrowth defined for " + stat);
        }
        return growth;
    }

    public static Collection<StatGrowth> all() {
        return GROWTH.values();
    }

    public static boolean isDefined(CombatStat stat) {
        return GROWTH.containsKey(stat);
    }

    private static void perPoint(CombatStat stat, Attribute owner, ValueType type,
                                 float base, float amount, float cap) {
        put(new StatGrowth(stat, owner, type, base, amount, 0f, cap));
    }

    private static void perFive(CombatStat stat, Attribute owner, ValueType type,
                                float base, float amount, float cap) {
        put(new StatGrowth(stat, owner, type, base, 0f, amount, cap));
    }

    private static void modifierOnly(CombatStat stat, ValueType type, float base) {
        put(new StatGrowth(stat, null, type, base, 0f, 0f, 0f));
    }

    private static void put(StatGrowth growth) {
        GROWTH.put(growth.stat(), growth);
    }

    static {
        // ---- Thew ----
        perPoint(CombatStat.MELEE_DAMAGE, Attribute.THEW, ValueType.FLAT, 0f, 2f, 0f);
        perPoint(CombatStat.MAX_BLOCK_POWER, Attribute.THEW, ValueType.FLAT, 0f, 2f, 0f);
        perPoint(CombatStat.CARRY_CAPACITY, Attribute.THEW, ValueType.FLAT, 0f, 5f, 0f);
        perFive(CombatStat.ARMOR_PENETRATION, Attribute.THEW, ValueType.PERCENT, 0f, 0.02f, 0.9f);
        perFive(CombatStat.MELEE_INJURY_CHANCE, Attribute.THEW, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perFive(CombatStat.STUN_CHANCE, Attribute.THEW, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perFive(CombatStat.FORTITUDE, Attribute.THEW, ValueType.PERCENT, 0f, 0.01f, 0.9f);

        // ---- Wind ----
        perPoint(CombatStat.COUNTER_CHANCE, Attribute.WIND, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perPoint(CombatStat.DODGE_CHANCE, Attribute.WIND, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perFive(CombatStat.MAX_STAMINA, Attribute.WIND, ValueType.FLAT, 0f, 5f, 0f);
        perFive(CombatStat.STAMINA_RECOVERY, Attribute.WIND, ValueType.FLAT, 0f, 1f, 0f);
        perFive(CombatStat.CRIT_CHANCE, Attribute.WIND, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perFive(CombatStat.ACCURACY, Attribute.WIND, ValueType.PERCENT, 1f, 0.01f, 0f);

        // ---- Flesh ----
        perPoint(CombatStat.MAX_HP, Attribute.FLESH, ValueType.FLAT, 50f, 30f, 0f);
        perPoint(CombatStat.INJURY_RESISTANCE, Attribute.FLESH, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perPoint(CombatStat.BLOCK_CHANCE, Attribute.FLESH, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perFive(CombatStat.HP_RESTORATION, Attribute.FLESH, ValueType.FLAT, 0f, 1f, 0f);
        perFive(CombatStat.BODY_PART_RESTORATION, Attribute.FLESH, ValueType.FLAT, 0f, 1f, 0f);

        // ---- Blood ----
        perPoint(CombatStat.MAX_BLOOD_POTENCY, Attribute.BLOOD, ValueType.FLAT, 0f, 5f, 0f);
        perPoint(CombatStat.BLOOD_POTENCY_RECOVERY, Attribute.BLOOD, ValueType.FLAT, 0f, 1f, 0f);
        perPoint(CombatStat.ELEMENTAL_INJURY_CHANCE, Attribute.BLOOD, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perFive(CombatStat.SPELL_POWER, Attribute.BLOOD, ValueType.PERCENT, 1f, 0.02f, 0f);
        perFive(CombatStat.SPELL_RANGE, Attribute.BLOOD, ValueType.FLAT, 0f, 1f, 0f);
        perFive(CombatStat.SPELL_AOE, Attribute.BLOOD, ValueType.FLAT, 0f, 1f, 0f);

        // ---- Grit ----
        perPoint(CombatStat.PAIN_RESISTANCE, Attribute.GRIT, ValueType.PERCENT, 0f, 0.01f, 0.9f);
        perFive(CombatStat.FATIGUE_GAIN, Attribute.GRIT, ValueType.PERCENT, 1f, -0.02f, 0f);

        // ---- Modifier-only ----
        modifierOnly(CombatStat.WEAPON_DAMAGE_MODIFIER, ValueType.PERCENT, 1f);
        modifierOnly(CombatStat.MAIN_HAND_EFFICIENCY, ValueType.PERCENT, 1f);
        modifierOnly(CombatStat.OFF_HAND_EFFICIENCY, ValueType.PERCENT, 0.8f);
        modifierOnly(CombatStat.CRIT_DAMAGE_BONUS, ValueType.PERCENT, 0.5f);
        modifierOnly(CombatStat.BLEED_CHANCE, ValueType.PERCENT, 0f);
        modifierOnly(CombatStat.KNOCKBACK_CHANCE, ValueType.PERCENT, 0f);
        modifierOnly(CombatStat.STAMINA_USAGE, ValueType.PERCENT, 1f);
        modifierOnly(CombatStat.COOLDOWN_MODIFIER, ValueType.PERCENT, 1f);
        modifierOnly(CombatStat.BLOCK_POWER_RECOVERY, ValueType.FLAT, 0f);
        modifierOnly(CombatStat.SPELL_CONTROL, ValueType.PERCENT, 1f);
        modifierOnly(CombatStat.SPELL_VOLATILITY, ValueType.PERCENT, 0f);
        modifierOnly(CombatStat.MOVE_COST, ValueType.PERCENT, 1f);
        modifierOnly(CombatStat.ACTION_TIME_COST_REDUCTION, ValueType.PERCENT, 0f);
        modifierOnly(CombatStat.RESIST_PHYSICAL, ValueType.PERCENT, 0f);
        modifierOnly(CombatStat.RESIST_FIRE, ValueType.PERCENT, 0f);
        modifierOnly(CombatStat.RESIST_LIGHTNING, ValueType.PERCENT, 0f);
        modifierOnly(CombatStat.RESIST_POISON, ValueType.PERCENT, 0f);
        modifierOnly(CombatStat.RESIST_ICE, ValueType.PERCENT, 0f);
    }
}
