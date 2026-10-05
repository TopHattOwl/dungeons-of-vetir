package com.tophattowl.dungeonsofvetir.game.items.handlers;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.components.ItemComponent;

/**
 * Per-component equip/unequip behavior. Each handler declares the component it
 * manages via {@link #componentType()}, so registration and dispatch are driven by
 * the handler itself
 * <p>
 * Handlers are for special per-item behavior only. Stats derived from equipment
 * should be recomputed on {@code EquipmentChangedEvent}, not applied here!!!
 */
public interface EquipHandler<T extends ItemComponent> {

    Class<T> componentType();

    void onEquip(Entity entity, Item item, T component);

    void onUnequip(Entity entity, Item item, T component);
}
