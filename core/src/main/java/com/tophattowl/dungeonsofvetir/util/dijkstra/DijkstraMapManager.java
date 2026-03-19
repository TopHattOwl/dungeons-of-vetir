package com.tophattowl.dungeonsofvetir.util.dijkstra;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.DijkstraMapUpdatedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityMovedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.util.Direction;
import com.tophattowl.dungeonsofvetir.util.dijkstra.maps.DijkstraMap;
import com.tophattowl.dungeonsofvetir.util.dijkstra.maps.FactionDijkstraMap;
import com.tophattowl.dungeonsofvetir.util.dijkstra.maps.PlayerDijkstraMap;

import java.util.*;

public class DijkstraMapManager {
    private final GameWorld gameWorld;
    private final List<EventBus.ListenerHandle<?>> listeners = new ArrayList<>();

    private final EnumMap<DijkstraMapType, DijkstraMap> dijkstraMaps = new EnumMap<>(DijkstraMapType.class);

    public DijkstraMapManager(GameWorld gameWorld) {
        this.gameWorld = gameWorld;


        dijkstraMaps.put(DijkstraMapType.PLAYER, new PlayerDijkstraMap(Level.WIDTH, Level.HEIGHT));
        dijkstraMaps.put(
            DijkstraMapType.FACTION_MONSTER,
            new FactionDijkstraMap(Level.WIDTH, Level.HEIGHT, Faction.MONSTER)
        );

        for (DijkstraMap dMap : dijkstraMaps.values()) {
            dMap.initialize(gameWorld);
            dMap.calculate();
        }

        listeners.add(EventBus.on(EntityMovedEvent.class, this::onEntityMoved));
        listeners.add(EventBus.on(EntityRemovedEvent.class, this::onEntityRemoved));
    }

    public int[][] getMap(DijkstraMapType mapType) {
        if (dijkstraMaps.containsKey(mapType)) return dijkstraMaps.get(mapType).map;
        else return new int[0][0];
    }

    public Direction getBestMove(int x, int y, EnumMap<DijkstraMapType, Integer> weightMap) {
        Direction bestMove = null;

        int bestScore = Integer.MAX_VALUE;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                if (dx == 0 && dy == 0) continue;

                int nx = x + dx, ny = y + dy;
                if (nx < 0 || ny < 0 || nx >= Level.WIDTH || ny >= Level.HEIGHT) continue;

                int score = 0;
                boolean blocked = false;

                for (Map.Entry<DijkstraMapType, Integer> entry : weightMap.entrySet()) {
                    if (entry.getValue() == 0) continue; // skip irrelevant maps

                    int[][] map = getMap(entry.getKey());
                    int value = map[nx][ny];

                    if (value == DijkstraMap.OBSTACLE_VALUE) {
                        blocked = true;
                        break;
                    }

                    int weight = entry.getValue();
                    score += weight * value;
                }

                if (blocked) continue;

                if (score < bestScore) {
                    bestScore = score;
                    bestMove = Direction.fromDxDy(dx, dy);
                }
            }
        }

        return bestMove;
    }

    private void onEntityMoved(EntityMovedEvent event) {
        Entity entity = event.entity();

        if (entity == gameWorld.getPlayer()) {
            DijkstraMap map = dijkstraMaps.get(DijkstraMapType.PLAYER);
            map.initialize(gameWorld);
            map.calculate();
            EventBus.emit(new DijkstraMapUpdatedEvent(DijkstraMapType.PLAYER));
        }
        Faction faction = entity.getComponent(IdentityComponent.class).faction;
        switch (faction) {
            case MONSTER -> {
                DijkstraMap map = dijkstraMaps.get(DijkstraMapType.FACTION_MONSTER);
                map.initialize(gameWorld);
                map.calculate();
                EventBus.emit(new DijkstraMapUpdatedEvent(DijkstraMapType.FACTION_MONSTER));
            }
        }

    }

    private void onEntityRemoved(EntityRemovedEvent event) {
        // TODO: complete
    }

    public void dispose() {
        listeners.forEach(EventBus::off);
    }
}
