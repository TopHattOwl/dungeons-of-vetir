package com.tophattowl.dungeonsofvetir.game.spawn;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;

import java.util.ArrayList;
import java.util.List;

public class SpawnConfig {
    public final Faction faction;
    public final int maxBudget;
    public final int budgetPerTurn;
    public final List<SpawnEntry> spawnPool;
    public final int minDistanceFromPlayer;

    public SpawnConfig(Builder builder) {
        this.faction = builder.faction;
        this.maxBudget = builder.maxBudget;
        this.budgetPerTurn = builder.budgetPerTurn;
        this.spawnPool = List.copyOf(builder.spawnPool);
        this.minDistanceFromPlayer = builder.minDistanceFromPlayer;
    }

    public static class SpawnEntry {
        public final ActorId actorId;
        public final int weight;

        public SpawnEntry(ActorId actorId, int weight) {
            this.actorId = actorId;
            this.weight = weight;
        }
    }

    public static class Builder {
        private final Faction faction;
        private int maxBudget = 50;
        private int budgetPerTurn = 5;
        private List<SpawnEntry> spawnPool = new ArrayList<>();
        private int minDistanceFromPlayer = 5;

        public Builder(Faction faction) {
            this.faction = faction;
        }

        public Builder maxBudget(int v) { this.maxBudget = v; return this; }
        public Builder budgetPerTurn(int v) { this.budgetPerTurn = v; return this; }
        public Builder addSpawnEntry(ActorId actorId, int weight) {
            this.spawnPool.add(new SpawnEntry(actorId, weight));
            return this;
        }
        public Builder minDistanceFromPlayer(int v) { this.minDistanceFromPlayer = v; return this; }

        public SpawnConfig build() {
            return new SpawnConfig(this);
        }
    }

    public static SpawnConfig defaultMonsterConfig() {
        return new Builder(Faction.MONSTER)
            .maxBudget(60)
            .budgetPerTurn(8)
            .addSpawnEntry(ActorId.IRON_WORM, 10)
            .minDistanceFromPlayer(5)
            .build();
    }

    public static SpawnConfig defaultLooterConfig() {
        return new Builder(Faction.LOOTER)
            .maxBudget(40)
            .budgetPerTurn(5)
            .addSpawnEntry(ActorId.SCAVENGER, 10)
            .minDistanceFromPlayer(6)
            .build();
    }
}
