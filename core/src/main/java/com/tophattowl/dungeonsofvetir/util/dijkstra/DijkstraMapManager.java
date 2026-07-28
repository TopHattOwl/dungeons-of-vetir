package com.tophattowl.dungeonsofvetir.util.dijkstra;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.EventSubscriptions;
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
    private final EventSubscriptions eventSubs = new EventSubscriptions();

    private final EnumMap<DijkstraMapType, DijkstraMap> dijkstraMaps = new EnumMap<>(DijkstraMapType.class);

    public DijkstraMapManager(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        addMaps();
        initMaps();
        addListeners();
    }

    public int[][] getMap(DijkstraMapType mapType) {
        if (dijkstraMaps.containsKey(mapType)) return dijkstraMaps.get(mapType).map;
        else return new int[0][0];
    }

    public int[][] getMapGoalsBlocked(DijkstraMapType mapType) {
        if (dijkstraMaps.containsKey(mapType)) return dijkstraMaps.get(mapType).getMapGoalsBlocked();
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

    public Direction getBestMove(int x, int y, EnumMap<DijkstraMapType, Integer> weightMap, Faction faction) {
        Direction bestMove = Direction.fromDxDy(0, 0);

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

                    DijkstraMap dijkstraMap = dijkstraMaps.get(entry.getKey());
                    int[][] map;

                    if (dijkstraMap instanceof FactionDijkstraMap factionDijkstraMap) {
                        FactionRelation.Relation relation = FactionRelation.getRelation(faction,
                            factionDijkstraMap.faction
                        );

                        if (relation == FactionRelation.Relation.HOSTILE) {
                            map = getMap(entry.getKey());
                        } else {
                            map = getMapGoalsBlocked(entry.getKey());
                        }
                    } else {
                        map = getMap(entry.getKey());
                    }

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
            updateDijkstraMap(DijkstraMapType.PLAYER);
        }
        Faction faction = entity.getComponent(IdentityComponent.class).faction;
        switch (faction) {
            case MONSTER -> updateDijkstraMap(DijkstraMapType.FACTION_MONSTER);
            case HUNTER -> updateDijkstraMap(DijkstraMapType.FACTION_HUNTER);
            case LOOTER -> updateDijkstraMap(DijkstraMapType.FACTION_LOOTER);
        }
    }

    private void updateDijkstraMap(DijkstraMapType mapType) {
        DijkstraMap map = dijkstraMaps.get(mapType);
        if (map == null) return;

        map.initialize(gameWorld);
        map.calculate();
        EventBus.emit(new DijkstraMapUpdatedEvent(mapType));
    }

    private void onEntityRemoved(EntityRemovedEvent event) {
        // TODO: complete
    }

    private void addMaps() {
        dijkstraMaps.put(DijkstraMapType.PLAYER, new PlayerDijkstraMap(Level.WIDTH, Level.HEIGHT));
        dijkstraMaps.put(
            DijkstraMapType.FACTION_MONSTER,
            new FactionDijkstraMap(Level.WIDTH, Level.HEIGHT, Faction.MONSTER)
        );
        dijkstraMaps.put(
            DijkstraMapType.FACTION_HUNTER,
            new FactionDijkstraMap(Level.WIDTH, Level.HEIGHT, Faction.HUNTER)
        );
        dijkstraMaps.put(
            DijkstraMapType.FACTION_LOOTER,
            new FactionDijkstraMap(Level.WIDTH, Level.HEIGHT, Faction.LOOTER)
        );
    }

    private void initMaps() {
        for (DijkstraMap dMap : dijkstraMaps.values()) {
            dMap.initialize(gameWorld);
            dMap.calculate();
        }
    }

    private void addListeners() {
        eventSubs.on(EntityMovedEvent.class, this::onEntityMoved);
        eventSubs.on(EntityRemovedEvent.class, this::onEntityRemoved);
    }

    public void dispose() {
        eventSubs.unsubscribeAll();
    }
}
