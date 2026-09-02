package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.ActionHandler;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonGenerator;
import com.tophattowl.dungeonsofvetir.game.dungeon.LevelPopulator;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityAddedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.factory.actors.EntityFactory;
import com.tophattowl.dungeonsofvetir.game.items.systems.ItemSystem;
import com.tophattowl.dungeonsofvetir.game.rng.SeedConfig;
import com.tophattowl.dungeonsofvetir.game.spawn.SpawnConfig;
import com.tophattowl.dungeonsofvetir.game.spawn.SpawnManager;
import com.tophattowl.dungeonsofvetir.game.spawn.SpawnManagerRegistry;
import com.tophattowl.dungeonsofvetir.game.turn_system.TimeTurnManager;
import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameWorld {
    private final long worldSeed;

    private final List<Entity> entities = new ArrayList<>();
    private final Entity[][] entityMap = new Entity[Level.WIDTH][Level.HEIGHT];

    private Level currentLevel;
    private final Entity player;

    private DungeonGenerator dungeonGenerator;
    public DijkstraMapManager dijkstraMapManager;
    public TimeTurnManager timeTurnManager;
    public SpawnManagerRegistry spawnManagerRegistry;

    public GameWorld(SeedConfig config) {
        this.worldSeed = config.getSeed();

        initialize();

        currentLevel = dungeonGenerator.generateLevel(1, worldSeed);
        Point playerSpawnPoint = findSpawn(currentLevel);
        player = EntityFactory.makePlayer(playerSpawnPoint);
        addEntity(player);

        spawnManagerRegistry = new SpawnManagerRegistry();
        spawnManagerRegistry.setGameWorld(this);
        spawnManagerRegistry.registerManager(new SpawnManager(SpawnConfig.defaultMonsterConfig()));
        spawnManagerRegistry.registerManager(new SpawnManager(SpawnConfig.defaultLooterConfig()));

        LevelPopulator.populate(this, 1);
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Returns all entities that have ALL the given component types
     * @param types component types
     * @return a list of entities
     */
    public List<Entity> query(Class<? extends Component>... types) {
        return entities.stream()
            .filter(e -> e.hasAllComponents(types))
            .collect(Collectors.toList());
    }

    public Entity getEntity(int x, int y) {
        return entityMap[x][y];
    }
    public Entity getEntity(Point point) {
        return entityMap[point.x][point.y];
    }
    public Entity getEntity(int id) {
        return entities.stream()
            .filter(e -> e.id == id)
            .findFirst()
            .orElse(null);
    }
    public List<Entity> getAllEntities() {
        return entities;
    }

    public void moveEntity(Entity entity, Point newPos) {
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        Point oldPos = posComp.getPosition();

        entityMap[oldPos.x][oldPos.y] = null;
        entityMap[newPos.x][newPos.y] = entity;
    }

    public Entity getPlayer() {
        return player;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        entityMap[posComp.getX()][posComp.getY()] = entity;
        EventBus.emit(new EntityAddedEvent(entity));
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        entityMap[posComp.getX()][posComp.getY()] = null;
        EventBus.emit(new EntityRemovedEvent(entity));
        entity.dispose();
    }

    public void addDijkstraMapManager(DijkstraMapManager dijkstraMapManager) {
        this.dijkstraMapManager = dijkstraMapManager;
    }

    private void initialize() {
        timeTurnManager = new TimeTurnManager();
        dungeonGenerator = new DungeonGenerator();

        ActionHandler.setGameWorld(this);

        FactionRelation.init();
    }

    private Point findSpawn(Level level) {
        // Try stairs_up first
        for (int x = 0; x < Level.WIDTH; x++)
            for (int y = 0; y < Level.HEIGHT; y++)
                if (level.getTile(x, y).type == TileType.STAIRS_UP)
                    return new Point(x, y);
        // Fall back to any walkable tile
        for (int x = 1; x < Level.WIDTH - 1; x++)
            for (int y = 1; y < Level.HEIGHT - 1; y++)
                if (level.isWalkable(x, y))
                    return new Point(x, y);
        return new Point(2, 2);
    }

    public void dispose() {
        dijkstraMapManager.dispose();
        timeTurnManager.dispose();
        spawnManagerRegistry.dispose();
    }
}
