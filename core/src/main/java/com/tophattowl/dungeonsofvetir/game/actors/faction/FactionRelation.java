package com.tophattowl.dungeonsofvetir.game.actors.faction;


import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;

import java.util.EnumMap;

public class FactionRelation {
    public enum Relation {
        FRIENDLY,
        NEUTRAL,
        HOSTILE,
    }
    public static final FactionRelation INSTANCE = new FactionRelation();

    private final EnumMap<Faction, EnumMap<Faction, Relation>> relations = new EnumMap<>(Faction.class);

    public FactionRelation() {}

    public static void init() {
        for (Faction faction : Faction.values()) {
            EnumMap<Faction, Relation> factionRelations = new EnumMap<>(Faction.class);

            for (Faction other : Faction.values()) {
                if (other == faction) factionRelations.put(other, Relation.FRIENDLY);
                else factionRelations.put(other, Relation.NEUTRAL);
            }

            INSTANCE.relations.put(faction, factionRelations);
        }
        setRelations(Faction.HUNTER, Faction.MONSTER, Relation.HOSTILE);
    }

    public static Relation getRelation(Faction faction, Faction other) {
        EnumMap<Faction, Relation> factionRelations = INSTANCE.relations.get(faction);
        return factionRelations.getOrDefault(other, Relation.NEUTRAL);
    }

    public static void setRelations(Faction a, Faction b, Relation relation) {
        INSTANCE.relations.get(a).put(b, relation);
        INSTANCE.relations.get(b).put(a, relation);
    }

    public static void logFactionRelations() {
        StringBuilder sb = new StringBuilder();
        sb.append("Faction Relations:\n");
        for (Faction faction : Faction.values()) {
            EnumMap<Faction, Relation> factionRelations = INSTANCE.relations.get(faction);
            sb.append(faction.name()).append(" : ").append(factionRelations).append("\n");
        }
        DebugLogger.log(DebugLogger.Category.FACTION, "FactionRelation", sb.toString());
    }
}
