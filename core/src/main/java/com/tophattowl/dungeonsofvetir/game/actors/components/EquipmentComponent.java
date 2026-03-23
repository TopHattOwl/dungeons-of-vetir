package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;

import java.util.ArrayList;
import java.util.List;

public class EquipmentComponent implements Component {

    public List<EquipmentSlot> equipment = new ArrayList<>();

    public EquipmentSlot getSpecific(BodyPart bodypart, EquipmentSlotType equipmentSlotType) {
        for (EquipmentSlot equipment : this.equipment) {
            if (bodypart.equals(equipment.bodyPart) && equipmentSlotType.equals(equipment.equipmentSlotType)) {
                return equipment;
            }
        }
        return null;
    }

    public EquipmentSlot getByBodyPart(BodyPart bodyPart) {
        for (EquipmentSlot equipment : this.equipment) {
            if (bodyPart.equals(equipment.bodyPart)) {
                return equipment;
            }
        }
        return null;
    }

    public void initSlots(BodyComponent bodyComp) {
        for (BodyPart bodypart : bodyComp.bodyParts) {
            if (bodypart.equippableSlots == null || bodypart.equippableSlots.isEmpty()) {
                continue;
            }
            addSlotsFromBodyPart(bodypart);
        }

        DebugLogger.log(DebugLogger.Category.EQUIPMENT, "EquipmentComponent",
            "Equipment slots made:\n" + this
        );
    }

    private void addSlotsFromBodyPart(BodyPart bodyPart) {
        for (EquipmentSlotType slot : bodyPart.equippableSlots) {
            equipment.add(new EquipmentSlot(bodyPart, slot));
        }
    }

    public static class EquipmentSlot {
        public final BodyPart bodyPart;
        public final EquipmentSlotType equipmentSlotType;
        public Item item;

        public EquipmentSlot(BodyPart bodyPart, EquipmentSlotType equipmentSlotType) {
            this(bodyPart, equipmentSlotType, null);
        }
        public EquipmentSlot(BodyPart bodyPart, EquipmentSlotType equipmentSlotType, Item item) {
            this.bodyPart = bodyPart;
            this.equipmentSlotType = equipmentSlotType;
            this.item = item;
        }

        @Override
        public String toString() {
            return item == null ?
                "{EquipmentSlot} | " + equipmentSlotType.toString() + " | " + bodyPart.name :
                "{EquipmentSlot} | " + equipmentSlotType.toString() + " | " + bodyPart.name + " | " + item;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EquipmentComponent:\n");
        for (EquipmentSlot equipmentSlot : equipment) {
            sb.append(equipmentSlot.toString()).append("\n");
        }
        return sb.toString();
    }
}
