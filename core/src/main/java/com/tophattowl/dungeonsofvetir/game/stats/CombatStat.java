package com.tophattowl.dungeonsofvetir.game.stats;

/**
 * How a stat is derived. Percent stats are stored as fractions (0.1 == 10%)
 * <p>
 * Every scalar the game derives from attributes, gear, conditions or expertise is a {@code CombatStat} <br>
 *
 * Resource maximums (max HP, max stamina, max blood potency) are {@code CombatStat}s too,
 * so a single modifier type can target anything <br>
 *
 * Stats with no {@link Attribute} owner are "modifier-only" (see {@link StatCatalog})
 */
public enum CombatStat implements StatRef {

    // Thew
    MELEE_DAMAGE,
    MAX_BLOCK_POWER,
    CARRY_CAPACITY,
    ARMOR_PENETRATION,
    MELEE_INJURY_CHANCE,
    STUN_CHANCE,
    FORTITUDE,

    // Wind
    COUNTER_CHANCE,
    DODGE_CHANCE,
    MAX_STAMINA,
    STAMINA_RECOVERY,
    CRIT_CHANCE,
    ACCURACY,

    // Flesh
    MAX_HP,
    INJURY_RESISTANCE,
    BLOCK_CHANCE,
    HP_RESTORATION,
    BODY_PART_RESTORATION,

    // Blood
    MAX_BLOOD_POTENCY,
    BLOOD_POTENCY_RECOVERY,
    ELEMENTAL_INJURY_CHANCE,
    SPELL_POWER,
    SPELL_RANGE,
    SPELL_AOE,

    // Grit
    PAIN_RESISTANCE,
    FATIGUE_GAIN,

    // Modifier-only (no attribute owner)
    WEAPON_DAMAGE_MODIFIER,
    MAIN_HAND_EFFICIENCY,
    OFF_HAND_EFFICIENCY,
    CRIT_DAMAGE_BONUS,
    BLEED_CHANCE,
    KNOCKBACK_CHANCE,
    STAMINA_USAGE,
    COOLDOWN_MODIFIER,
    BLOCK_POWER_RECOVERY,
    SPELL_CONTROL,
    SPELL_VOLATILITY,
    MOVE_COST,
    ACTION_TIME_COST_REDUCTION,
    RESIST_PHYSICAL,
    RESIST_FIRE,
    RESIST_LIGHTNING,
    RESIST_POISON,
    RESIST_ICE,
}
