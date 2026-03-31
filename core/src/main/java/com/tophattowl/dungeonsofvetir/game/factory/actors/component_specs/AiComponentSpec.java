package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.AiComponent;
import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapType;

public record AiComponentSpec(
    int playerDijkstraWeight,
    int monsterDijkstraWeight,
    int hunterDijkstraWeight,
    int looterDijkstraWeight
) implements ActorComponentSpec<AiComponent> {

    @Override
    public Class<AiComponent> getComponentType() {
        return null;
    }

    @Override
    public AiComponent build(Entity entity) {
        AiComponent comp = new AiComponent();
        comp.setWeight(DijkstraMapType.PLAYER, playerDijkstraWeight);
        comp.setWeight(DijkstraMapType.FACTION_MONSTER, monsterDijkstraWeight);
        comp.setWeight(DijkstraMapType.FACTION_HUNTER, hunterDijkstraWeight);
        comp.setWeight(DijkstraMapType.FACTION_LOOTER, looterDijkstraWeight);
        return comp;
    }
}
