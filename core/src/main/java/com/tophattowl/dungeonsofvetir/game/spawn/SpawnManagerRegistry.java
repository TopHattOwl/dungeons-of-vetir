package com.tophattowl.dungeonsofvetir.game.spawn;

import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.EnumMap;
import java.util.Map;

public class SpawnManagerRegistry {
    private final Map<Faction, SpawnManager> managers = new EnumMap<>(Faction.class);
    private GameWorld gameWorld;

    public SpawnManagerRegistry() {
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void registerManager(SpawnManager manager) {
        managers.put(manager.getConfig().faction, manager);
    }

    public SpawnManager getManager(Faction faction) {
        return managers.get(faction);
    }

    public SpawnManager getMonsterManager() {
        return managers.get(Faction.MONSTER);
    }

    public SpawnManager getLooterManager() {
        return managers.get(Faction.LOOTER);
    }


    public void dispose() {
        managers.values().forEach(SpawnManager::dispose);
        managers.clear();
    }
}
