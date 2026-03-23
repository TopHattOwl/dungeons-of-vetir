package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.systems.EquipSystem;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class EquipAction extends Action {
    private final Item item;
    private final BodyPart targetBodyPart;
    private final EquipmentSlotType equipmentSlotType;

    public EquipAction(Entity owner, Item item, BodyPart targetBodyPart, EquipmentSlotType equipmentSlotType) {
        super(ActionType.EQUIP, owner);
        this.item = item;
        this.targetBodyPart = targetBodyPart;
        this.equipmentSlotType = equipmentSlotType;
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

    public EquipmentSlotType getEquipmentSlotType() {
        return equipmentSlotType;
    }

    public BodyPart getTargetBodyPart() {
        return targetBodyPart;
    }

    @Override
    public String toString() {
        return "[EquipAction]: " +
            "item=" + item +
            ", cost=" + cost +
            ", owner=" + owner;
    }
}
