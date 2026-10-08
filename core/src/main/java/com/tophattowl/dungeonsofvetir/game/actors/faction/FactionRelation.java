package com.tophattowl.dungeonsofvetir.game.actors.faction;

import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;

import java.util.EnumMap;

/**
 * Per-run faction relationship table, owned by a world
 */
public class FactionRelation {

    public enum Relation {
        FRIENDLY,
        NEUTRAL,
        HOSTILE,
    }

    private final EnumMap<Faction, EnumMap<Faction, Relation>> relations = new EnumMap<>(Faction.class);

    public FactionRelation() {
        for (Faction faction : Faction.values()) {
            EnumMap<Faction, Relation> row = new EnumMap<>(Faction.class);
            for (Faction other : Faction.values()) {
                row.put(other, other == faction ? Relation.FRIENDLY : Relation.NEUTRAL);
            }
            relations.put(faction, row);
        }
        setRelations(Faction.HUNTER, Faction.MONSTER, Relation.HOSTILE);
    }

    public Relation getRelation(Faction faction, Faction other) {
        EnumMap<Faction, Relation> row = relations.get(faction);
        return row.getOrDefault(other, Relation.NEUTRAL);
    }

    public void setRelations(Faction a, Faction b, Relation relation) {
        relations.get(a).put(b, relation);
        relations.get(b).put(a, relation);
    }

    public void logFactionRelations() {
        StringBuilder sb = new StringBuilder();
        sb.append("Faction Relations:\n");
        for (Faction faction : Faction.values()) {
            sb.append(faction.name()).append(" : ").append(relations.get(faction)).append("\n");
        }
        DebugLogger.log(DebugLogger.Category.FACTION, "FactionRelation", sb.toString());
    }
}
