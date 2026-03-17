package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.ai.AiStrategy;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public class AiComponent implements Component {
    public AiStrategy aiStrategy;
    public Point lastKnownPlayerPos;

    public AiComponent(AiStrategy aiStrategy) {
        this.aiStrategy = aiStrategy;
        this.lastKnownPlayerPos = null;
    }
}
