package com.tophattowl.dungeonsofvetir.game.world;

import com.tophattowl.dungeonsofvetir.game.meta.MetaState;
import com.tophattowl.dungeonsofvetir.game.meta.RunMode;
import com.tophattowl.dungeonsofvetir.game.system.TurnSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Run-level container. Owns the current zone ({@link GameWorld}), the run's meta state/mode, and the ordered turn pipeline.
 * Grows to hold the world map and per-dungeon state as that content lands
 */
public class WorldContext {

    private final GameWorld world;
    private final MetaState meta;
    private final RunMode runMode;
    private final List<TurnSystem> turnSystems = new ArrayList<>();

    public WorldContext(GameWorld world, MetaState meta) {
        this.world = world;
        this.meta = meta;
        this.runMode = RunMode.forMeta(meta);
        world.timeTurnManager.setTurnEndListener(this::processTurn);
        addSystem(world.constraints());
    }

    public GameWorld world() {
        return world;
    }

    public MetaState meta() {
        return meta;
    }

    public RunMode runMode() {
        return runMode;
    }

    public void addSystem(TurnSystem system) {
        turnSystems.add(system);
    }

    /**
     * Ordered world turn-end pipeline: runs every system in registration order.
     * Driven by the turn clock ({@code TimeTurnManager.passTurn}), not by the player.
     * Currently holds the {@link ConstraintSystem}; buffs/projectiles join later.
     */
    public void processTurn() {
        for (TurnSystem system : turnSystems) {
            system.process(this);
        }
    }
}
