package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.systems.EquipSystem;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class EquipAction extends Action {
    private final Item item;


    public EquipAction(Entity owner, Item item) {
        super(ActionType.EQUIP, owner);
        this.item = item;
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        return gameWorld.getItemSystem(EquipSystem.class).prepareEquip(this, gameWorld);
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return gameWorld.getItemSystem(EquipSystem.class).executeEquip(this, gameWorld);
    }

    public Item getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "[EquipAction]: " +
            "item=" + item +
            ", cost=" + cost +
            ", owner=" + owner;
    }
}
