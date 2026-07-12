package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.ItemType;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class InventoryComponent implements Component {
    public float maxWeight;
    public float currentWeight;

    public EnumMap<ItemType, List<Item>> inventory = new EnumMap<>(ItemType.class);

    public InventoryComponent(float maxWeight) {
        this.maxWeight = maxWeight;

         for (ItemType itemType : ItemType.values()) {
             inventory.put(itemType, new ArrayList<>());
         }
    }
}
