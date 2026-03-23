package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class SwapEquipmentAction extends Action {
    private final Item item;
    private final Item itemToUnequip;
    private final BodyPart targetBodyPart;

    public SwapEquipmentAction(Entity owner, Item item, Item itemToUnequip, BodyPart targetBodyPart) {
        super(ActionType.SWAP_EQUIPMENT, owner);
        this.item = item;
        this.itemToUnequip = itemToUnequip;
        this.targetBodyPart = targetBodyPart;
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        return null;
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return null;
    }
}
