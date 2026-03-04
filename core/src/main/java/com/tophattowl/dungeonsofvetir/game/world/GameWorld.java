package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.*;
import com.tophattowl.dungeonsofvetir.game.ECS.systems.GameSystem;
import com.tophattowl.dungeonsofvetir.game.ECS.systems.MovementSystem;
import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * the central game state, owns all entities, current level and the systems
 *
 */
public class GameWorld {
    private List<Entity> entities = new ArrayList<>();
    private List<GameSystem> systems = new ArrayList<>();

    private Level currentLevel;
    private Entity player;

    private DungeonGenerator dungeonGenerator;

    private List<String> messageLog = new ArrayList<>();

    public GameWorld() {
        // PLACEHOLDER fast player making for now
        player = new Entity();
        player.addComponent(new PlayerComponent())
                .addComponent(new PositionComponent(0, 0))
                .addComponent(new EnergyComponent())
                .addComponent(new FovComponent(10, Level.WIDTH, Level.HEIGHT))
                .addComponent(new RenderableComponent("player", 10));

        entities.add(player);

        systems.add(new MovementSystem());

        dungeonGenerator = new DungeonGenerator();
        //placeholder level
        currentLevel = dungeonGenerator.generateLevel(1);

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

    public Entity getPlayer() {
        return player;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void addSystem(GameSystem system) {
        systems.add(system);
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
}
