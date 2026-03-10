package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;

public class HealthComponent implements Component {
    public int maxHp;
    public int hp;

    public HealthComponent(int maxHp) {
        this.maxHp = maxHp;
        this.hp = maxHp;
    }
}
