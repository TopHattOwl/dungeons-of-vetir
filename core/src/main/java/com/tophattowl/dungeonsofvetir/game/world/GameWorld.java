package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonDefinition;
import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonGenerator;
import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonRegistry;
import com.tophattowl.dungeonsofvetir.game.dungeon.DungeonState;
import com.tophattowl.dungeonsofvetir.game.dungeon.LevelPopulator;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.ResolvedFloor;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityAddedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.LevelChangedEvent;
import com.tophattowl.dungeonsofvetir.game.ECS.systems.FovSystem;
import com.tophattowl.dungeonsofvetir.game.factory.actors.EntityFactory;
import com.tophattowl.dungeonsofvetir.game.generation.HashUtil;
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
    private Entity[][] entityMap;
    private List<Exit> currentExits = new ArrayList<>();

    private Level currentLevel;
    private int currentFloor;
    private Place currentPlace;
    private ResolvedFloor currentResolved;
    private final Entity player;
    private final FovSystem fovSystem;
    private final FactionRelation factionRelations = new FactionRelation();

    private DungeonGenerator dungeonGenerator;
    private ZoneResolver zoneResolver;
    private ConstraintSystem constraintSystem;
    private DungeonState currentDungeon;
    public DijkstraMapManager dijkstraMapManager;
    public TimeTurnManager timeTurnManager;
    public SpawnManagerRegistry spawnManagerRegistry;

    public GameWorld(SeedConfig config) {
        this.worldSeed = config.getSeed();
        this.fovSystem = new FovSystem(this);

        initialize();

        currentPlace = dungeonGenerator.resolveSurfacePlace();
        currentFloor = currentPlace.depth();
        currentResolved = null;
        currentLevel = zoneResolver.generate(currentPlace);
        entityMap = new Entity[currentLevel.getWidth()][currentLevel.getHeight()];
        currentExits = buildExits(currentPlace, currentLevel);
        Point playerSpawnPoint = findSpawn(currentLevel);
        player = EntityFactory.makePlayer(currentLevel, playerSpawnPoint);
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

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ResolvedFloor getCurrentResolved() {
        return currentResolved;
    }

    public Place getCurrentPlace() {
        return currentPlace;
    }

    public DungeonState getCurrentDungeon() {
        return currentDungeon;
    }

    public FactionRelation getFactionRelations() {
        return factionRelations;
    }

    public ConstraintSystem constraints() {
        return constraintSystem;
    }

    /**
     * Transitions the world to the given dungeon floor. Convenience wrapper around
     * {@link #enterPlace(Place)} for the linear dungeon.
     */
    public void enterLevel(int floorNumber) {
        if (floorNumber < 1) return;
        enterPlace(dungeonGenerator.resolvePlace(floorNumber));
    }

    /**
     * Transitions the world to a place: regenerates the zone, removes every entity
     * except the player, repositions the player, resizes per-zone state, resets the
     * turn clock, and recomputes FOV + Dijkstra maps. Single entry point for stairs,
     * overworld travel and debug warp.
     */
    public void enterPlace(Place place) {
        enterPlace(place, null);
    }

    /**
     * Transitions the world to a place: regenerates the zone, removes every entity
     * except the player, assembles exits, repositions the player, resizes per-zone
     * state, resets the turn clock, and recomputes Dijkstra maps. Single entry point
     * for stairs, overworld travel and debug warp.
     *
     * @param arrivalFrom the transition kind that led here, used to spawn on the
     *                    matching tile; {@code null} spawns at the default entrance
     */
    public void enterPlace(Place place, TransitionKind arrivalFrom) {
        if (place == null) return;

        currentPlace = place;
        currentFloor = place.depth();
        currentResolved = place.isDungeonFloor() ? dungeonGenerator.resolve(place.depth()) : null;
        currentLevel = zoneResolver.generate(place);

        for (Entity entity : new ArrayList<>(entities)) {
            if (entity != player) removeEntity(entity);
        }

        entityMap = new Entity[currentLevel.getWidth()][currentLevel.getHeight()];
        currentExits = buildExits(place, currentLevel);

        Point spawn = resolveSpawn(currentLevel, arrivalFrom);
        player.getComponent(PositionComponent.class).set(spawn);
        entityMap[spawn.x][spawn.y] = player;

        FovComponent playerFov = player.getComponent(FovComponent.class);
        if (playerFov != null) {
            playerFov.resize(currentLevel.getWidth(), currentLevel.getHeight());
            playerFov.clearExplored();
        }

        timeTurnManager.reset(this);
        if (dijkstraMapManager != null) dijkstraMapManager.rebuild();

        EventBus.emit(new LevelChangedEvent(place));
    }

    /**
     * Scans the generated level for transition tiles and resolves their targets.
     * Stairs link a dungeon floor to its neighbours; declared exits (doors, etc.)
     * arrive with authored zones.
     */
    private List<Exit> buildExits(Place place, Level level) {
        List<Exit> exits = new ArrayList<>();

        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                TileType type = level.getTile(x, y).type;
                if (type == TileType.STAIRS_DOWN) {
                    Place target = descendTarget(place);
                    if (target != null) {
                        exits.add(new Exit(new Point(x, y), TransitionKind.STAIRS_DOWN, target));
                    }
                } else if (type == TileType.STAIRS_UP) {
                    Place target = ascendTarget(place);
                    if (target != null) {
                        exits.add(new Exit(new Point(x, y), TransitionKind.STAIRS_UP, target));
                    }
                }
            }
        }

        for (DeclaredExit declared : level.getDeclaredExits()) {
            exits.add(new Exit(declared.tile(), declared.kind(), resolveDeclaredTarget(declared, place)));
        }

        return exits;
    }

    /**
     * Resolves a declared exit target to a Place. The instance key encodes the
     * template and its origin (so repeated templates get distinct, deterministic
     * content), and the seed is mixed from stable primitives only.
     */
    private Place resolveDeclaredTarget(DeclaredExit declared, Place source) {
        String instanceKey = declared.targetKey()
            + "@" + source.id().key() + ":" + source.depth()
            + ":" + declared.tile().x + "," + declared.tile().y;

        long seed = HashUtil.mix(worldSeed, instanceKey);
        seed = HashUtil.mix(seed, declared.targetKind());

        return new Place(
            new PlaceId(declared.targetKind(), instanceKey, 0, declared.tile().x, declared.tile().y),
            Level.DEFAULT_WIDTH,
            Level.DEFAULT_HEIGHT,
            seed,
            TileTheme.CAVES_DANK,
            PlaceRole.NORMAL
        );
    }

    private Place descendTarget(Place place) {
        if (place.kind() == PlaceKind.DUNGEON_SURFACE) {
            return dungeonGenerator.resolvePlace(1);
        }
        if (place.isDungeonFloor() && dungeonGenerator.hasFloor(place.depth() + 1)) {
            return dungeonGenerator.resolvePlace(place.depth() + 1);
        }
        return null;
    }

    private Place ascendTarget(Place place) {
        if (place.isDungeonFloor()) {
            if (place.depth() > 1) return dungeonGenerator.resolvePlace(place.depth() - 1);
            return dungeonGenerator.resolveSurfacePlace();
        }
        return null; // surface up -> overworld later
    }

    private Point resolveSpawn(Level level, TransitionKind arrivalFrom) {
        if (arrivalFrom != null) {
            TileType arrivalTile = switch (arrivalFrom) {
                case STAIRS_DOWN -> TileType.STAIRS_UP;   // descending arrives at the up-stairs
                case STAIRS_UP -> TileType.STAIRS_DOWN;   // ascending arrives at the down-stairs
                default -> null;                          // doors: reciprocal tile (authored zones)
            };
            if (arrivalTile != null) {
                Point tile = findTile(level, arrivalTile);
                if (tile != null) return tile;
            }
        }
        return findSpawn(level);
    }

    private Point findTile(Level level, TileType type) {
        for (int x = 0; x < level.getWidth(); x++) {
            for (int y = 0; y < level.getHeight(); y++) {
                if (level.getTile(x, y).type == type) return new Point(x, y);
            }
        }
        return null;
    }

    public List<Exit> getExits() {
        return currentExits;
    }

    public Exit exitAt(Point tile) {
        for (Exit exit : currentExits) {
            if (exit.tile().equals(tile)) return exit;
        }
        return null;
    }

    public void useExit(Exit exit) {
        if (exit == null) return;
        enterPlace(exit.target(), exit.kind());
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
        DungeonDefinition definition = DungeonRegistry.defaultDungeon();
        currentDungeon = new DungeonState(definition.key(), worldSeed);
        dungeonGenerator = new DungeonGenerator(definition, worldSeed);
        zoneResolver = new ZoneResolver(dungeonGenerator);
        constraintSystem = new ConstraintSystem(this);
    }

    private Point findSpawn(Level level) {
        // Try stairs_up first
        for (int x = 0; x < level.getWidth(); x++)
            for (int y = 0; y < level.getHeight(); y++)
                if (level.getTile(x, y).type == TileType.STAIRS_UP)
                    return new Point(x, y);
        // Fall back to any walkable tile
        for (int x = 1; x < level.getWidth() - 1; x++)
            for (int y = 1; y < level.getHeight() - 1; y++)
                if (level.isWalkable(x, y))
                    return new Point(x, y);
        return new Point(2, 2);
    }

    public void dispose() {
        fovSystem.dispose();
        dijkstraMapManager.dispose();
        timeTurnManager.dispose();
        spawnManagerRegistry.dispose();
    }
}
