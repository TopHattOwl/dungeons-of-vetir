package com.tophattowl.dungeonsofvetir.game.items.behavior;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public interface EquipableBehavior {
    public void onEquip(Entity wearer, GameWorld gameWorld);
    public void onUnequip(Entity wearer, GameWorld gameWorld);
}
