package com.tophattowl.dungeonsofvetir.game.items.behavior;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public interface UsableBehavior {
    public void onUse(Entity user, Entity target, GameWorld gameWorld);
}
