package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class SwapEquipmentAction extends Action {
    private final Item item;
    private final BodyPart targetBodyPart;
    private final List<Item> itemsToUnequip;

    public SwapEquipmentAction(Entity owner, Item item, BodyPart targetBodyPart) {
        super(ActionType.SWAP_EQUIPMENT, owner);
        this.item = item;
        this.targetBodyPart = targetBodyPart;
        this.itemsToUnequip = new ArrayList<>();
    }

    public void addItemToUnequip(Item item) {
        this.itemsToUnequip.add(item);
    }

    public Item getItem() {
        return item;
    }

    public BodyPart getTargetBodyPart() {
        return targetBodyPart;
    }

    public List<Item> getItemsToUnequip() {
        return itemsToUnequip;
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
