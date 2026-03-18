package com.tophattowl.dungeonsofvetir.util.dijkstra.maps;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;

public class FactionDijkstraMap extends DijkstraMap {
    public final Faction faction;

    public FactionDijkstraMap(int width, int height, Faction faction) {
        super(width, height);
        this.faction = faction;
    }

    @Override
    public void initialize(GameWorld gameWorld) {
        Level level = gameWorld.getCurrentLevel();

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                initTile(x, y, gameWorld, level);
            }
        }
    }

    private void initTile(int x, int y, GameWorld gameWorld, Level level) {
        Entity entity = gameWorld.getEntityAt(x, y);

        // if entity is at pos, check faction
        if (entity != null) {
            Faction entityFaction = entity.getComponent(IdentityComponent.class).faction;
            if (entityFaction == faction) {
                map[x][y] = GOAL_VALUE;
                return;
            }
        }

        if (level.isWalkable(x, y)) {
            map[x][y] = BASE_VALUE;
            return;
        }

        map[x][y] = OBSTACLE_VALUE;
    }
}
