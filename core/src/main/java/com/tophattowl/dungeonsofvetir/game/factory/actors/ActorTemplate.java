package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;

/**
 * EntityFactory reads this and builds the actual Entity
 */
public class ActorTemplate {
    // --- IDENTITY ---
    public final ActorId actorId;
    public final String name;

    // --- SPAWNING ---
    public final int spawnCost;

    // --- RENDERABLE ---
    public final String spriteId;
    public final int renderOrder;

    // --- BODY ---
    public final BodyTemplate bodyTemplate;

    // --- HEALTH ---
    public final int maxHp;

    // --- TIME VALUE ---
    public final float baseSpeed;

    // --- FOV ---
    public final int visionRange;

    private ActorTemplate(Builder b) {
        this.actorId = b.actorId;
        this.name = b.name;
        this.spawnCost = b.spawnCost;
        this.spriteId = b.spriteId;
        this.renderOrder = b.renderOrder;
        this.bodyTemplate = b.bodyTemplate;
        this.maxHp = b.maxHp;
        this.baseSpeed = b.baseSpeed;
        this.visionRange = b.visionRange;
    }

    public static class Builder {
        // required
        private final ActorId actorId;
        private final String name;

        // defaults
        public int spawnCost = 10;
        public String spriteId = "unknown";
        public int renderOrder = 1;
        public BodyTemplate bodyTemplate = BodyTemplate.HUMANOID;
        public int maxHp = 100;
        public float baseSpeed = 1.0f;
        public int visionRange = 8;

        public Builder(ActorId actorId, String name) {
            this.actorId = actorId;
            this.name = name;
        }

        public Builder spawnCost(int v) {this.spawnCost = v; return this; }
        public Builder spriteId(String v) { this.spriteId = v; return this; }
        public Builder renderOrder(int v) {this.renderOrder = v; return this; }
        public Builder bodyTemplate(BodyTemplate v) {this.bodyTemplate = v; return this; }
        public Builder maxHp(int v) {this.maxHp = v; return this; }
        public Builder baseSpeed(float v) {this.baseSpeed = v; return this; }
        public Builder visionRange(int v) {this.visionRange = v; return this; }

        public ActorTemplate build() {
            return new ActorTemplate(this);
        }

    }
}
