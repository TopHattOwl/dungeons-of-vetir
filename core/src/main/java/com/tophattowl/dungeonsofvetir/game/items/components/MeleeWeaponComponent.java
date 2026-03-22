package com.tophattowl.dungeonsofvetir.game.items.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.items.behavior.EquipableBehavior;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class MeleeWeaponComponent implements ItemComponent, EquipableBehavior {

    public int damage;


    @Override
    public void onEquip(Entity wearer, GameWorld gameWorld) {

    }

    @Override
    public void onUnequip(Entity wearer, GameWorld gameWorld) {

    }
}
