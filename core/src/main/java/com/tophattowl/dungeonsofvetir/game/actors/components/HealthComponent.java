package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartStatus;

public class HealthComponent implements Component {
    public int maxHp;
    public int hp;
    public HealthStatus status;

    public HealthComponent(int maxHp) {
        this.maxHp = maxHp;
        hp = maxHp;
        status = HealthStatus.HEALTHY;
    }

    public boolean takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
        updateStatus();
        return hp == 0;
    }

    public void heal(int amount) {
        hp = Math.min(hp + amount, maxHp);
        updateStatus();
    }

    private void updateStatus() {
        float ratio = (float) hp/maxHp;

        if (ratio <= HealthStatus.CRITICAL.threshold)       setStatus(HealthStatus.CRITICAL);
        else if (ratio <= HealthStatus.INJURED.threshold)   setStatus(HealthStatus.INJURED);
        else if (ratio <= HealthStatus.HURT.threshold)      setStatus(HealthStatus.HURT);
        else if (ratio <= HealthStatus.FINE.threshold)      setStatus(HealthStatus.FINE);
        else                                                setStatus(HealthStatus.HEALTHY);
    }

    private void setStatus(HealthStatus status) {
        this.status = status;
        // TODO: maybe emit events when critical, ai -> start fleeing, player -> visual effect or something
    }

    public enum HealthStatus {
        HEALTHY(0.9f),
        FINE(0.7f),
        HURT(0.5f),
        INJURED(0.35f),
        CRITICAL(0.2f),
        ;

        public final float threshold;
        HealthStatus(float threshold) {
            this.threshold = threshold;
        }
    }

}
