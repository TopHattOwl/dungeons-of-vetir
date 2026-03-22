package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlot;
import com.tophattowl.dungeonsofvetir.game.items.Item;

import java.util.EnumMap;

public class EquipmentComponent implements Component {

    public EnumMap<EquipmentSlot, Item> equipment = new EnumMap<>(EquipmentSlot.class);

    // TODO: equipment comp get built based on body component's body parts
}
