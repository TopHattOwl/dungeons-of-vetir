package com.tophattowl.dungeonsofvetir.game.factory.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.*;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.util.Direction;

public class ActionFactory {

    public static PassAction createPassAction(Entity owner) {
        return new PassAction(owner);
    }

    public static MoveAction createMoveAction(Entity owner, Direction dir) {
        return new MoveAction(dir, owner);
    }

    public static MeleeAttackAction createMeleeAttackAction(Entity owner, Entity target) {
        return new MeleeAttackAction(owner, target);
    }

    public static EquipAction createEquipAction(Entity owner, Item item,
                                                BodyPart bodyPart, EquipmentSlotType slotType) {
        return new EquipAction(owner, item, bodyPart, slotType);
    }

    public static UnequipAction createUnequipAction(Entity owner, Item item,
                                                    BodyPart bodyPart) {
        return new UnequipAction();
    }

    public static SwapEquipmentAction createSwapEquipmentAction(Entity owner, Item item,
                                                                BodyPart bodyPart) {
        return new SwapEquipmentAction(owner, item, bodyPart);
    }
}
