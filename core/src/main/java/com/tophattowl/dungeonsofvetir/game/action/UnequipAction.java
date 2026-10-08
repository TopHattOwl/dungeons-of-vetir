package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.systems.EquipSystem;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

/**
 * Removes an equipped item from its slot(s). Possible only while the item is worn.
 */
public class UnequipAction extends Action {

    private final Item item;

    public UnequipAction(Entity owner, Item item) {
        super(ActionType.UNEQUIP, owner);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        if (!gameWorld.constraints().can(owner, Capability.EQUIP)) return this;
        return EquipSystem.prepareUnequip(this, gameWorld);
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return EquipSystem.executeUnequip(this, gameWorld);
    }

    @Override
    public String toString() {
        return "[UnequipAction]: item=" + item + ", cost=" + cost;
    }
}
