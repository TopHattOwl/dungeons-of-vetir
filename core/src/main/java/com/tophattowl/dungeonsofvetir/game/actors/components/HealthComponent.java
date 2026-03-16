package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartStatus;

public class HealthComponent implements Component {
    private int maxHp;
    public int hp;
    public HealthStatus status;

    public HealthComponent(int maxHp) {
        this.maxHp = maxHp;
        hp = maxHp;
        status = HealthStatus.HEALTHY;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public boolean takeDamage(int damage) {
        hp = Math.max(0, hp - damage);
        updateStatus();
        return hp == 0;
    }

    private void updateStatus() {
        float ratio = (float) hp/maxHp;

        if (ratio <= HealthStatus.CRITICAL.threshold)       status = HealthStatus.CRITICAL;
        else if (ratio <= HealthStatus.INJURED.threshold)   status = HealthStatus.INJURED;
        else if (ratio <= HealthStatus.HURT.threshold)      status = HealthStatus.HURT;
        else if (ratio <= HealthStatus.FINE.threshold)      status = HealthStatus.FINE;
        else                                                status = HealthStatus.HEALTHY;
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
