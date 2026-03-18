package com.tophattowl.dungeonsofvetir.util.dijkstra;

import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityMovedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DijkstraMapManager {
    private final GameWorld gameWorld;
    private final List<EventBus.ListenerHandle<?>> listeners = new ArrayList<>();

    public PlayerDijkstraMap playerDijkstraMap;
    public FactionDijkstraMap monsterFactionDijkstraMap;

    public DijkstraMapManager(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        playerDijkstraMap = new PlayerDijkstraMap(Level.WIDTH, Level.HEIGHT);
        playerDijkstraMap.initialize(gameWorld);
        playerDijkstraMap.calculate();

        monsterFactionDijkstraMap = new FactionDijkstraMap(Level.WIDTH, Level.HEIGHT, Faction.MONSTER);
        monsterFactionDijkstraMap.initialize(gameWorld);
        monsterFactionDijkstraMap.calculate();

        listeners.add(EventBus.on(EntityMovedEvent.class, this::onEntityMoved));
    }

    private void onEntityMoved(EntityMovedEvent event) {
        if (event.entity() == gameWorld.getPlayer()) {
            playerDijkstraMap.initialize(gameWorld);
            playerDijkstraMap.calculate();

            monsterFactionDijkstraMap.initialize(gameWorld);
            monsterFactionDijkstraMap.calculate();

            playerDijkstraMap.logDijkstraMap();
            monsterFactionDijkstraMap.logDijkstraMap();
        }
    }

    public void dispose() {
        listeners.forEach(EventBus::off);
    }
}
