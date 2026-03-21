package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.factory.actors.ActorRegistry;
import com.tophattowl.dungeonsofvetir.game.factory.actors.EntityFactory;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class LevelPopulator {
    private static final int MIN_SPAWN_DISTANCE_FROM_PLAYER = 5;

    public static void populate(GameWorld gameWorld, int floorNumber) {
        Level level = gameWorld.getCurrentLevel();
        List<Point> validSpawnTiles = collectValidSpawnTiles(level, gameWorld);

        if (validSpawnTiles.isEmpty()) {
            return;
        }

        int monsterCount = calculateSpawnCount(floorNumber, 3, 8);
        int looterCount = calculateSpawnCount(floorNumber, 2, 5);

        spawnEntities(gameWorld, validSpawnTiles, floorNumber, monsterCount, looterCount);
    }

    private static List<Point> collectValidSpawnTiles(Level level, GameWorld gameWorld) {
        List<Point> tiles = new ArrayList<>();

        for (int x = 1; x < Level.WIDTH - 1; x++) {
            for (int y = 1; y < Level.HEIGHT - 1; y++) {
                if (!level.isWalkable(x, y)) continue;
                if (gameWorld.getEntityAt(x, y) != null) continue;
                tiles.add(new Point(x, y));
            }
        }

        return tiles;
    }

    private static int calculateSpawnCount(int floorNumber, int baseMin, int baseMax) {
        int min = baseMin + (floorNumber - 1) / 2;
        int max = baseMax + (floorNumber - 1);
        return min + RandomGenerator.getDefault().nextInt(max - min + 1);
    }

    private static void spawnEntities(GameWorld gameWorld, List<Point> validTiles, int floorNumber,
                                       int monsterCount, int looterCount) {
        Entity player = gameWorld.getPlayer();
        PositionComponent playerPos = player.getComponent(PositionComponent.class);
        Point playerPoint = new Point(playerPos.getX(), playerPos.getY());

        List<Point> farTiles = validTiles.stream()
            .filter(p -> distance(p, playerPoint) >= MIN_SPAWN_DISTANCE_FROM_PLAYER)
            .toList();

        if (farTiles.isEmpty()) {
            farTiles = validTiles;
        }

        List<Point> shuffled = new ArrayList<>(farTiles);
        shuffleList(shuffled);

        int spawned = 0;
        int tileIndex = 0;

        while (spawned < monsterCount && tileIndex < shuffled.size()) {
            Point spawnPos = shuffled.get(tileIndex++);
            if (canSpawnAt(spawnPos, gameWorld)) {
                ActorId actorId = selectMonsterActor(floorNumber);
                EntityFactory.createEntity(actorId, gameWorld, spawnPos);
                spawned++;
            }
        }

        List<Point> allShuffled = new ArrayList<>(validTiles);
        shuffleList(allShuffled);
        tileIndex = 0;
        spawned = 0;

        while (spawned < looterCount && tileIndex < allShuffled.size()) {
            Point spawnPos = allShuffled.get(tileIndex++);
            if (canSpawnAt(spawnPos, gameWorld) && distance(spawnPos, playerPoint) >= MIN_SPAWN_DISTANCE_FROM_PLAYER) {
                ActorId actorId = selectLooterActor(floorNumber);
                EntityFactory.createEntity(actorId, gameWorld, spawnPos);
                spawned++;
            }
        }
    }

    private static boolean canSpawnAt(Point pos, GameWorld gameWorld) {
        if (!gameWorld.getCurrentLevel().isWalkable(pos.x, pos.y)) {
            return false;
        }
        return gameWorld.getEntityAt(pos) == null;
    }

    private static ActorId selectMonsterActor(int floorNumber) {
        List<ActorId> available = new ArrayList<>();

        if (ActorRegistry.get(ActorId.IRON_WORM) != null) {
            available.add(ActorId.IRON_WORM);
        }
        if (available.isEmpty()) {
            return ActorId.IRON_WORM;
        }

        return available.get(RandomGenerator.getDefault().nextInt(available.size()));
    }

    private static ActorId selectLooterActor(int floorNumber) {
        if (ActorRegistry.get(ActorId.SCAVENGER) != null) {
            return ActorId.SCAVENGER;
        }
        return ActorId.SCAVENGER;
    }

    private static double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private static <T> void shuffleList(List<T> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            int j = RandomGenerator.getDefault().nextInt(i + 1);
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }
}
