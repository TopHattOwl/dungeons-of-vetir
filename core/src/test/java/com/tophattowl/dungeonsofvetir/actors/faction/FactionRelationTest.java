package com.tophattowl.dungeonsofvetir.actors.faction;

import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation.Relation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FactionRelationTest {

    private FactionRelation relations;

    @BeforeEach
    void setUp() {
        relations = new FactionRelation();
    }

    @Test
    void init_SetsSameFactionFriendly() {
        assertEquals(Relation.FRIENDLY, relations.getRelation(Faction.HUNTER, Faction.HUNTER));
        assertEquals(Relation.FRIENDLY, relations.getRelation(Faction.MONSTER, Faction.MONSTER));
        assertEquals(Relation.FRIENDLY, relations.getRelation(Faction.LOOTER, Faction.LOOTER));
    }

    @Test
    void init_SetsDefaultToNeutral() {
        assertEquals(Relation.NEUTRAL, relations.getRelation(Faction.HUNTER, Faction.LOOTER));
        assertEquals(Relation.NEUTRAL, relations.getRelation(Faction.LOOTER, Faction.HUNTER));
        assertEquals(Relation.NEUTRAL, relations.getRelation(Faction.MONSTER, Faction.LOOTER));
        assertEquals(Relation.NEUTRAL, relations.getRelation(Faction.LOOTER, Faction.MONSTER));
    }

    @Test
    void init_SetsHunterMonsterHostile() {
        assertEquals(Relation.HOSTILE, relations.getRelation(Faction.HUNTER, Faction.MONSTER));
        assertEquals(Relation.HOSTILE, relations.getRelation(Faction.MONSTER, Faction.HUNTER));
    }

    @Test
    void getRelation_AllFactionCombinations() {
        for (Faction faction : Faction.values()) {
            for (Faction other : Faction.values()) {
                Relation relation = relations.getRelation(faction, other);
                assertNotNull(relation);
            }
        }
    }

    @Test
    void setRelations_Symmetric() {
        relations.setRelations(Faction.HUNTER, Faction.LOOTER, Relation.HOSTILE);

        assertEquals(Relation.HOSTILE, relations.getRelation(Faction.HUNTER, Faction.LOOTER));
        assertEquals(Relation.HOSTILE, relations.getRelation(Faction.LOOTER, Faction.HUNTER));
    }

    @Test
    void setRelations_PersistsAfterSet() {
        relations.setRelations(Faction.MONSTER, Faction.LOOTER, Relation.FRIENDLY);

        assertEquals(Relation.FRIENDLY, relations.getRelation(Faction.MONSTER, Faction.LOOTER));
        assertEquals(Relation.FRIENDLY, relations.getRelation(Faction.LOOTER, Faction.MONSTER));
    }

    @Test
    void setRelations_CanChangeToNeutral() {
        relations.setRelations(Faction.HUNTER, Faction.MONSTER, Relation.NEUTRAL);

        assertEquals(Relation.NEUTRAL, relations.getRelation(Faction.HUNTER, Faction.MONSTER));
        assertEquals(Relation.NEUTRAL, relations.getRelation(Faction.MONSTER, Faction.HUNTER));
    }

    @Test
    void instances_AreIndependent() {
        relations.setRelations(Faction.HUNTER, Faction.MONSTER, Relation.NEUTRAL);

        FactionRelation fresh = new FactionRelation();
        assertEquals(Relation.HOSTILE, fresh.getRelation(Faction.HUNTER, Faction.MONSTER));
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
