package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.systems.EquipSystem;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Equips an item into a slot that is already occupied: the listed items are
 * removed first, then the new item is equipped.
 */
public class SwapEquipmentAction extends Action {

    private final Item item;
    private final BodyPart targetBodyPart;
    private final EquipmentSlotType equipmentSlotType;
    private final List<Item> itemsToUnequip;

    public SwapEquipmentAction(Entity owner, Item item, BodyPart targetBodyPart,
                               EquipmentSlotType equipmentSlotType) {
        super(ActionType.SWAP_EQUIPMENT, owner);
        this.item = item;
        this.targetBodyPart = targetBodyPart;
        this.equipmentSlotType = equipmentSlotType;
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

    public EquipmentSlotType getEquipmentSlotType() {
        return equipmentSlotType;
    }

    public List<Item> getItemsToUnequip() {
        return itemsToUnequip;
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        return EquipSystem.prepareSwap(this, gameWorld);
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return EquipSystem.executeSwap(this, gameWorld);
    }

    @Override
    public String toString() {
        return "[SwapEquipmentAction]: item=" + item
            + ", unequip=" + itemsToUnequip
            + ", cost=" + cost;
    }
}
