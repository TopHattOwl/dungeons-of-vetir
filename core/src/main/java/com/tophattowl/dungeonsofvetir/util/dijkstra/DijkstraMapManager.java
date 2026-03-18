package com.tophattowl.dungeonsofvetir.util.dijkstra;

import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityMovedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DijkstraMapManager {
    public PlayerDijkstraMap playerDijkstraMap;
    private GameWorld gameWorld;
    private final List<EventBus.ListenerHandle<?>> listeners = new ArrayList<>();

    public DijkstraMapManager(GameWorld gameWorld, PlayerDijkstraMap playerDijkstraMap) {
        this.playerDijkstraMap = playerDijkstraMap;
        this.gameWorld = gameWorld;

        playerDijkstraMap.initialize(gameWorld);
        playerDijkstraMap.calculate(gameWorld);

        for (int[] row : playerDijkstraMap.map) {
            System.out.println(Arrays.toString(row));
        }

        listeners.add(EventBus.on(EntityMovedEvent.class, this::onEntityMoved));
    }

    private void onEntityMoved(EntityMovedEvent event) {
        if (event.entity() == gameWorld.getPlayer()) {
            playerDijkstraMap.initialize(gameWorld);
            playerDijkstraMap.calculate(gameWorld);
            for (int[] row : playerDijkstraMap.map) {
                System.out.println(Arrays.toString(row));
            }
        }
    }
}
