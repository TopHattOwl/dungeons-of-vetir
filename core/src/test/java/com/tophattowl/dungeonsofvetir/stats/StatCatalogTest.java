package com.tophattowl.dungeonsofvetir.stats;

import com.tophattowl.dungeonsofvetir.game.stats.CombatStat;
import com.tophattowl.dungeonsofvetir.game.stats.StatCatalog;
import com.tophattowl.dungeonsofvetir.game.stats.StatGrowth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatCatalogTest {

    @Test
    void everyCombatStatHasGrowth() {
        for (CombatStat stat : CombatStat.values()) {
            assertTrue(StatCatalog.isDefined(stat), "missing growth for " + stat);
            assertNotNull(StatCatalog.get(stat));
        }
    }

    @Test
    void allReturnsEveryStat() {
        assertEquals(CombatStat.values().length, StatCatalog.all().size());
    }

    @Test
    void modifierOnlyStatsHaveNoOwner() {
        assertTrue(StatCatalog.get(CombatStat.WEAPON_DAMAGE_MODIFIER).isModifierOnly());
        assertTrue(StatCatalog.get(CombatStat.RESIST_FIRE).isModifierOnly());
        assertFalse(StatCatalog.get(CombatStat.MAX_HP).isModifierOnly());
    }

    @Test
    void baseValuesAreSensible() {
        assertEquals(1f, StatCatalog.get(CombatStat.MAIN_HAND_EFFICIENCY).base(), 1e-6f);
        assertEquals(0.8f, StatCatalog.get(CombatStat.OFF_HAND_EFFICIENCY).base(), 1e-6f);
        assertEquals(1f, StatCatalog.get(CombatStat.ACCURACY).base(), 1e-6f);
        assertEquals(50f, StatCatalog.get(CombatStat.MAX_HP).base(), 1e-6f);
    }

    @Test
    void growthTiersAreExclusive() {
        StatGrowth perPoint = StatCatalog.get(CombatStat.MAX_HP);
        assertEquals(0f, perPoint.perFive(), 1e-6f);

        StatGrowth perFive = StatCatalog.get(CombatStat.ACCURACY);
        assertEquals(0f, perFive.perPoint(), 1e-6f);
    }
}
