package com.tophattowl.dungeonsofvetir.actors.faction;

import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation.Relation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FactionRelationTest {

    @BeforeEach
    void setUp() {
        FactionRelation.init();
    }

    @AfterEach
    void tearDown() {
        FactionRelation.init();
    }

    @Test
    void init_SetsSameFactionFriendly() {
        assertEquals(Relation.FRIENDLY, FactionRelation.getRelation(Faction.HUNTER, Faction.HUNTER));
        assertEquals(Relation.FRIENDLY, FactionRelation.getRelation(Faction.MONSTER, Faction.MONSTER));
        assertEquals(Relation.FRIENDLY, FactionRelation.getRelation(Faction.LOOTER, Faction.LOOTER));
    }

    @Test
    void init_SetsDefaultToNeutral() {
        assertEquals(Relation.NEUTRAL, FactionRelation.getRelation(Faction.HUNTER, Faction.LOOTER));
        assertEquals(Relation.NEUTRAL, FactionRelation.getRelation(Faction.LOOTER, Faction.HUNTER));
        assertEquals(Relation.NEUTRAL, FactionRelation.getRelation(Faction.MONSTER, Faction.LOOTER));
        assertEquals(Relation.NEUTRAL, FactionRelation.getRelation(Faction.LOOTER, Faction.MONSTER));
    }

    @Test
    void init_SetsHunterMonsterHostile() {
        assertEquals(Relation.HOSTILE, FactionRelation.getRelation(Faction.HUNTER, Faction.MONSTER));
        assertEquals(Relation.HOSTILE, FactionRelation.getRelation(Faction.MONSTER, Faction.HUNTER));
    }

    @Test
    void getRelation_AllFactionCombinations() {
        for (Faction faction : Faction.values()) {
            for (Faction other : Faction.values()) {
                Relation relation = FactionRelation.getRelation(faction, other);
                assertNotNull(relation);
            }
        }
    }

    @Test
    void setRelations_Symmetric() {
        FactionRelation.setRelations(Faction.HUNTER, Faction.LOOTER, Relation.HOSTILE);

        assertEquals(Relation.HOSTILE, FactionRelation.getRelation(Faction.HUNTER, Faction.LOOTER));
        assertEquals(Relation.HOSTILE, FactionRelation.getRelation(Faction.LOOTER, Faction.HUNTER));
    }

    @Test
    void setRelations_PersistsAfterSet() {
        FactionRelation.setRelations(Faction.MONSTER, Faction.LOOTER, Relation.FRIENDLY);

        assertEquals(Relation.FRIENDLY, FactionRelation.getRelation(Faction.MONSTER, Faction.LOOTER));
        assertEquals(Relation.FRIENDLY, FactionRelation.getRelation(Faction.LOOTER, Faction.MONSTER));
    }

    @Test
    void setRelations_CanChangeToNeutral() {
        FactionRelation.setRelations(Faction.HUNTER, Faction.MONSTER, Relation.NEUTRAL);

        assertEquals(Relation.NEUTRAL, FactionRelation.getRelation(Faction.HUNTER, Faction.MONSTER));
        assertEquals(Relation.NEUTRAL, FactionRelation.getRelation(Faction.MONSTER, Faction.HUNTER));
    }

    @Test
    void relationValues() {
        assertNotNull(Relation.FRIENDLY);
        assertNotNull(Relation.NEUTRAL);
        assertNotNull(Relation.HOSTILE);
        assertEquals(3, Relation.values().length);
    }

    @Test
    void factionValues() {
        assertEquals(4, Faction.values().length);
    }
}
