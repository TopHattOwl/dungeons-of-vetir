package com.tophattowl.dungeonsofvetir.actors.components;

import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.DefensiveStatsComponent;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class DefensiveStatsComponentTest {

    @Test
    void constructor_InitializesResistances() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(50, 0.1f, 0.05f, body);

        assertNotNull(defense.resistances);
        assertEquals(ElementType.values().length, defense.resistances.size());
    }

    @Test
    void constructor_InitializesResistancesToZero() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(50, 0.1f, 0.05f, body);

        for (ElementType type : ElementType.values()) {
            assertEquals(0.0f, defense.resistances.get(type));
        }
    }

    @Test
    void constructor_InitializesProtections() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(50, 0.1f, 0.05f, body);

        assertNotNull(defense.protections);
        assertEquals(body.bodyParts.size(), defense.protections.size());
    }

    @Test
    void constructor_InitializesProtectionsToZero() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(50, 0.1f, 0.05f, body);

        for (Map.Entry<com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart, Integer> entry : defense.protections.entrySet()) {
            assertEquals(0, entry.getValue());
        }
    }

    @Test
    void constructor_SetsEvasion() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(75, 0.1f, 0.05f, body);
        assertEquals(75, defense.evasion);
    }

    @Test
    void constructor_SetsCounterChance() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(50, 0.2f, 0.1f, body);
        assertEquals(0.2f, defense.counterChance);
    }

    @Test
    void constructor_SetsBlockChance() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(50, 0.1f, 0.15f, body);
        assertEquals(0.15f, defense.blockChance);
    }

    @Test
    void resistances_ContainsAllElementTypes() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(50, 0.1f, 0.05f, body);

        assertTrue(defense.resistances.containsKey(ElementType.PHYSICAL));
        assertTrue(defense.resistances.containsKey(ElementType.FIRE));
        assertTrue(defense.resistances.containsKey(ElementType.LIGHTNING));
        assertTrue(defense.resistances.containsKey(ElementType.POISON));
    }

    @Test
    void protections_ContainsAllBodyParts() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent defense = new DefensiveStatsComponent(50, 0.1f, 0.05f, body);

        for (var part : body.bodyParts) {
            assertTrue(defense.protections.containsKey(part), "Should contain protection for " + part.name);
        }
    }

    @Test
    void player_DefaultDefense() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        DefensiveStatsComponent playerDefense = new DefensiveStatsComponent(100, 0.1f, 0.1f, body);

        assertEquals(100, playerDefense.evasion);
        assertEquals(0.1f, playerDefense.counterChance);
        assertEquals(0.1f, playerDefense.blockChance);
    }

    @Test
    void monster_DefaultDefense() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.WORM, 120, null);
        DefensiveStatsComponent monsterDefense = new DefensiveStatsComponent(50, 0.05f, 0.05f, body);

        assertEquals(50, monsterDefense.evasion);
        assertEquals(0.05f, monsterDefense.counterChance);
        assertEquals(0.05f, monsterDefense.blockChance);
    }
}
