package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.combat.CombatSystem;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.ECS.systems.MovementSystem;
import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonGenerator;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityAddedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.factory.actors.EntityFactory;
import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameWorld {
    private final List<Entity> entities = new ArrayList<>();
    private final List<GameSystem> systems = new ArrayList<>();

    private final Entity[][] entityMap = new Entity[Level.WIDTH][Level.HEIGHT];

    private Level currentLevel;
    private final Entity player;

    private DungeonGenerator dungeonGenerator;
    public DijkstraMapManager dijkstraMapManager;

    public GameWorld() {

        player = EntityFactory.makePlayer();
        addEntity(player);

        // PLACEHOLDER enemy
        EntityFactory.createEntity(ActorId.IRON_WORM, this, new Point(12, 12));
//        EntityFactory.createEntity(ActorId.IRON_WORM, this, new Point(11, 11));

        addSystem(new MovementSystem());
        addSystem(new CombatSystem());

        dungeonGenerator = new DungeonGenerator();

        //placeholder level
        currentLevel = dungeonGenerator.generateLevel(1);

        FactionRelation.init();

        System.out.println(FactionRelation.getRelation(Faction.MONSTER, Faction.HUNTER));
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Returns all entities that have ALL the given component types
     * @param types component types
     * @return a list of entities
     */
    public List<Entity> querry(Class<?>... types) {
        return entities.stream()
            .filter(e -> e.hasAllComponents(types))
            .collect(Collectors.toList());
    }

    public Entity getEntityAt(int x, int y) {
        return entityMap[x][y];
    }
    public Entity getEntityAt(Point point) {
        return entityMap[point.x][point.y];
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
    }

    public void addSystem(GameSystem system) {
        systems.add(system);
    }

    public void addDijkstraMapManager(DijkstraMapManager dijkstraMapManager) {
        this.dijkstraMapManager = dijkstraMapManager;
    }

    @SuppressWarnings("unchecked")
    public <T extends GameSystem> T getSystem(Class<T> type) {
        for (GameSystem system : systems) {
            if (system.getClass().equals(type)) {
                return (T) system;
            }
        }
        return null;
    }

    public void dispose() {
        dijkstraMapManager.dispose();
    }
}
