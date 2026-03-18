package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.ai.EntityBrain;
import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapType;

import java.util.EnumMap;

public class AiComponent implements Component {
    public EntityBrain entityBrain;
    public EnumMap<DijkstraMapType, Integer> weightMap;

    public AiComponent() {
        this.entityBrain = new EntityBrain();
        this.weightMap = new EnumMap<>(DijkstraMapType.class);

        for (DijkstraMapType mapType : DijkstraMapType.values()) {
            this.weightMap.put(mapType, 0);
        }
    }

    public AiComponent(EnumMap<DijkstraMapType, Integer> weightMap) {
        this.weightMap = weightMap;
        this.entityBrain = new EntityBrain();
    }

    public void setWeight(DijkstraMapType type, int value) {
        weightMap.put(type, value);
    }
}
