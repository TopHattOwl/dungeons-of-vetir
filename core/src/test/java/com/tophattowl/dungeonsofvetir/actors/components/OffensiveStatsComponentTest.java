package com.tophattowl.dungeonsofvetir.actors.components;

import com.tophattowl.dungeonsofvetir.game.actors.components.OffensiveStatsComponent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OffensiveStatsComponentTest {

    @Test
    void constructor_SetsAllFields() {
        OffensiveStatsComponent stats = new OffensiveStatsComponent(30, 1.2f, 0.9f, 1.1f, 100);

        assertEquals(30, stats.baseDamage);
        assertEquals(1.2f, stats.weaponDamageModifier);
        assertEquals(0.9f, stats.mainHandEfficiency);
        assertEquals(1.1f, stats.offHandEfficiencyModifier);
        assertEquals(100, stats.accuracy);
    }

    @Test
    void constructor_DefaultValues() {
        OffensiveStatsComponent stats = new OffensiveStatsComponent(20, 1.0f, 1.0f, 1.0f, 50);

        assertEquals(20, stats.baseDamage);
        assertEquals(1.0f, stats.weaponDamageModifier);
        assertEquals(1.0f, stats.mainHandEfficiency);
        assertEquals(1.0f, stats.offHandEfficiencyModifier);
        assertEquals(50, stats.accuracy);
    }

    @Test
    void fields_CanBeModified() {
        OffensiveStatsComponent stats = new OffensiveStatsComponent(20, 1.0f, 1.0f, 1.0f, 50);

        stats.baseDamage = 25;
        stats.weaponDamageModifier = 1.5f;
        stats.mainHandEfficiency = 1.2f;
        stats.offHandEfficiencyModifier = 0.8f;
        stats.accuracy = 75;

        assertEquals(25, stats.baseDamage);
        assertEquals(1.5f, stats.weaponDamageModifier);
        assertEquals(1.2f, stats.mainHandEfficiency);
        assertEquals(0.8f, stats.offHandEfficiencyModifier);
        assertEquals(75, stats.accuracy);
    }

    @Test
    void player_DefaultStats() {
        OffensiveStatsComponent playerStats = new OffensiveStatsComponent(30, 1.0f, 1.0f, 1.0f, 100);

        assertEquals(30, playerStats.baseDamage);
        assertEquals(100, playerStats.accuracy);
    }

    @Test
    void monster_DefaultStats() {
        OffensiveStatsComponent monsterStats = new OffensiveStatsComponent(20, 1.0f, 1.0f, 1.0f, 50);

        assertEquals(20, monsterStats.baseDamage);
        assertEquals(50, monsterStats.accuracy);
    }
}
