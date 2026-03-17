package com.tophattowl.dungeonsofvetir.game.ai;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public abstract class AiStrategy {
    protected int priority;

    public AiStrategy(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public abstract Action chooseAction(Entity entity, GameWorld world);
}
