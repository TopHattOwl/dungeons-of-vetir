package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * EntityFactory reads this and builds the actual Entity
 */
public class ActorTemplate {
    // --- IDENTITY ---
    public final ActorId actorId;
    public final String name;
    public final Faction faction;

    // --- SPAWNING ---
    public final int spawnCost;

    // --- RENDERABLE ---
    public final String spriteId;
    public final int renderOrder;

    // --- BODY ---
    public final BodyTemplate bodyTemplate;
    public final Map<String, Integer> naturalProtections;

    // --- HEALTH ---
    public final int maxHp;

    // --- TIME VALUE ---
    public final float baseSpeed;

    // --- FOV ---
    public final int visionRange;

    // --- OFFENSIVE STATS ---
    public final int baseDamage;
    public final float weaponDamageModifier;
    public final float mainHandEfficiency;
    public final float offHandEfficiencyModifier;
    public final int accuracy;

    // --- DEFENSIVE STATS ---
    public final int evasion;
    public final float counterChance;
    public final float blockChance;

    // --- AI ---
    public final int playerDijkstraWeight;
    public final int monsterDijkstraWeight;
    public final int hunterDijkstraWeight;
    public final int looterDijkstraWeight;

    private ActorTemplate(Builder b) {
        this.actorId = b.actorId;
        this.name = b.name;
        this.faction = b.faction;
        this.spawnCost = b.spawnCost;
        this.spriteId = b.spriteId;
        this.renderOrder = b.renderOrder;
        this.bodyTemplate = b.bodyTemplate;
        this.naturalProtections = b.naturalProtections;
        this.maxHp = b.maxHp;
        this.baseSpeed = b.baseSpeed;
        this.visionRange = b.visionRange;
        this.baseDamage = b.baseDamage;
        this.weaponDamageModifier = b.weaponDamageModifier;
        this.mainHandEfficiency = b.mainHandEfficiency;
        this.offHandEfficiencyModifier = b.offHandEfficiencyModifier;
        this.accuracy = b.accuracy;
        this.evasion = b.evasion;
        this.counterChance = b.counterChance;
        this.blockChance = b.blockChance;
        this.playerDijkstraWeight = b.playerDijkstraWeight;
        this.monsterDijkstraWeight = b.monsterDijkstraWeight;
        this.hunterDijkstraWeight = b.hunterDijkstraWeight;
        this.looterDijkstraWeight = b.looterDijkstraWeight;
    }

    public static class Builder {
        // required
        private final ActorId actorId;
        private final String name;
        private final Faction faction;

        // defaults
        public int spawnCost = 10;
        public String spriteId = "unknown";
        public int renderOrder = 1;
        public BodyTemplate bodyTemplate = BodyTemplate.HUMANOID;
        public Map<String, Integer> naturalProtections = new HashMap<>();
        public int maxHp = 100;
        public float baseSpeed = 1.0f;
        public int visionRange = 8;
        public int baseDamage = 25;
        public float weaponDamageModifier = 1.0f;
        public float mainHandEfficiency = 1.0f;
        public float offHandEfficiencyModifier = 0.9f;
        public int accuracy = 50;
        public int evasion = 20;
        public float counterChance = 0.05f;
        public float blockChance = 0.05f;
        public int playerDijkstraWeight = 0;
        public int monsterDijkstraWeight = 0;
        public int hunterDijkstraWeight = 0;
        public int looterDijkstraWeight = 0;


        public Builder(ActorId actorId, String name, Faction faction) {
            this.actorId = actorId;
            this.name = name;
            this.faction = faction;
        }

        public Builder spawnCost(int v) {this.spawnCost = v; return this; }
        public Builder spriteId(String v) { this.spriteId = v; return this; }
        public Builder renderOrder(int v) {this.renderOrder = v; return this; }
        public Builder bodyTemplate(BodyTemplate v) {this.bodyTemplate = v; return this; }
        public Builder naturalProtections(String bodyPartName, int prots) {this.naturalProtections.put(bodyPartName, prots); return this; }
        public Builder maxHp(int v) {this.maxHp = v; return this; }
        public Builder baseSpeed(float v) {this.baseSpeed = v; return this; }
        public Builder visionRange(int v) {this.visionRange = v; return this; }
        public Builder baseDamage(int v) {this.baseDamage = v; return this; }
        public Builder weaponDamageModifier(float v) {this.weaponDamageModifier = v; return this; }
        public Builder mainHandEfficiency(float v) {this.mainHandEfficiency = v; return this; }
        public Builder offHandEfficiencyModifier(float v) {this.offHandEfficiencyModifier = v; return this; }
        public Builder accuracy(int v) {this.accuracy = v; return this; }
        public Builder evasion(int v) {this.evasion = v; return this; }
        public Builder counterChance(int v) {this.counterChance = v; return this; }
        public Builder blockChance(int v) {this.blockChance = v; return this; }
        public Builder playerDijkstraWeight(int v) {this.playerDijkstraWeight = v; return this; }
        public Builder monsterDijkstraWeight(int v) {this.monsterDijkstraWeight = v; return this; }
        public Builder hunterDijkstraWeight(int v) {this.hunterDijkstraWeight = v; return this; }
        public Builder looterDijkstraWeight(int v) {this.looterDijkstraWeight = v; return this; }

        public ActorTemplate build() {
            return new ActorTemplate(this);
        }

    }
}
