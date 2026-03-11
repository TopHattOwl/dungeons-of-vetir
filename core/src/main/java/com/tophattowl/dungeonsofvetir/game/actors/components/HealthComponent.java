package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;

/**
 * maxHp is only used to calculate body part hp
 * when max hp changes recalculate body part hp
 */
public class HealthComponent implements Component {
    private int maxHp;

    public HealthComponent(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    /**
     * changes max hp by the given amount
     */
    public void changeMaxHp(int amount) {
        this.maxHp += amount;
    }
}
