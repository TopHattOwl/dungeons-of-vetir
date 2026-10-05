package com.tophattowl.dungeonsofvetir.game.event.events.item;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.event.events.Event;
import com.tophattowl.dungeonsofvetir.game.items.Item;

/**
 * Emitted whenever an item is equipped or unequipped (a swap emits both) <br>
 * Derived stats should be recomputed in response to this instead of per-component handlers
 */
public record EquipmentChangedEvent(Entity entity, Item item, boolean equipped) implements Event {
}
