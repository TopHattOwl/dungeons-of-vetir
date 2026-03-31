package com.tophattowl.dungeonsofvetir.game.spawn;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.TurnPassedEvent;
import com.tophattowl.dungeonsofvetir.game.factory.actors.ActorRegistry;
import com.tophattowl.dungeonsofvetir.game.factory.actors.EntityFactory;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;

public class SpawnManager {
    private final SpawnConfig config;
    private final AtomicInteger currentBudget;
    private final List<EventBus.ListenerHandle<?>> listeners = new ArrayList<>();

    public SpawnManager(SpawnConfig config) {
        this.config = config;
        this.currentBudget = new AtomicInteger(0);
        addListeners();
    }

    public SpawnConfig getConfig() {
        return config;
    }

    public int getCurrentBudget() {
        return currentBudget.get();
    }

    public int getMaxBudget() {
        return config.maxBudget;
    }

    public boolean canSpawn(ActorId actorId) {
        var spec = ActorRegistry.get(actorId);
        return currentBudget.get() >= spec.spawnCost();
    }

    public boolean trySpawn(GameWorld gameWorld, ActorId actorId) {
        if (!canSpawn(actorId)) {
            return false;
        }

        var spec = ActorRegistry.get(actorId);
        Point spawnPos = findValidSpawnPosition(gameWorld);

        if (spawnPos == null) {
            DebugLogger.log(DebugLogger.Category.SPAWN, "SpawnManager",
                "No valid spawn position found for " + actorId
            );
            return false;
        }

        currentBudget.addAndGet(-spec.spawnCost());
        EntityFactory.createEntity(actorId, gameWorld, spawnPos);

        DebugLogger.log(DebugLogger.Category.SPAWN, "SpawnManager",
            "Spawned " + actorId + " at " + spawnPos + ", budget: " + currentBudget.get() + "/" + config.maxBudget
        );

        return true;
    }

    public boolean trySpawnRandom(GameWorld gameWorld) {
        if (config.spawnPool.isEmpty()) {
            return false;
        }

        List<SpawnConfig.SpawnEntry> candidates = config.spawnPool.stream()
            .filter(entry -> canSpawn(entry.actorId))
            .toList();

        if (candidates.isEmpty()) {
            return false;
        }

        int totalWeight = candidates.stream().mapToInt(e -> e.weight).sum();
        int roll = RandomGenerator.getDefault().nextInt(totalWeight);

        int cumulative = 0;
        for (SpawnConfig.SpawnEntry entry : candidates) {
            cumulative += entry.weight;
            if (roll < cumulative) {
                return trySpawn(gameWorld, entry.actorId);
            }
        }

        return false;
    }

    private Point findValidSpawnPosition(GameWorld gameWorld) {
        Level level = gameWorld.getCurrentLevel();
        Entity player = gameWorld.getPlayer();
        PositionComponent playerPos = player.getComponent(PositionComponent.class);

        List<Point> validPositions = new ArrayList<>();

        for (int x = 1; x < Level.WIDTH - 1; x++) {
            for (int y = 1; y < Level.HEIGHT - 1; y++) {
                if (!level.isWalkable(x, y)) continue;
                if (gameWorld.getEntityAt(x, y) != null) continue;

                double dist = Math.sqrt(
                    Math.pow(x - playerPos.getX(), 2) +
                    Math.pow(y - playerPos.getY(), 2)
                );

                if (dist >= config.minDistanceFromPlayer) {
                    validPositions.add(new Point(x, y));
                }
            }
        }

        if (validPositions.isEmpty()) {
            return null;
        }

        return validPositions.get(RandomGenerator.getDefault().nextInt(validPositions.size()));
    }

    private void tick() {
        int newBudget = Math.min(config.maxBudget, currentBudget.get() + config.budgetPerTurn);
        currentBudget.set(newBudget);
    }

    private void onEntityRemoved(EntityRemovedEvent event) {
        if (event.entity().hasComponent(PositionComponent.class)) {
            PositionComponent pos = event.entity().getComponent(PositionComponent.class);
            if (pos != null && !levelContainsEntityAt(pos.getX(), pos.getY())) {
            }
        }
    }

    private boolean levelContainsEntityAt(int x, int y) {
        return false;
    }

    private void addListeners() {
        listeners.add(EventBus.on(TurnPassedEvent.class, e -> tick()));
    }

    public void dispose() {
        listeners.forEach(EventBus::off);
        listeners.clear();
    }
}
