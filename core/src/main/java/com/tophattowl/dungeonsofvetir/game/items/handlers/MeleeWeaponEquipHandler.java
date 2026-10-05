package com.tophattowl.dungeonsofvetir.game.items.handlers;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;

/**
 * Equip/unequip behavior for melee weapons
 */
public class MeleeWeaponEquipHandler implements EquipHandler<MeleeWeaponComponent> {

    @Override
    public Class<MeleeWeaponComponent> componentType() {
        return MeleeWeaponComponent.class;
    }

    @Override
    public void onEquip(Entity entity, Item item, MeleeWeaponComponent component) {
    }

    @Override
    public void onUnequip(Entity entity, Item item, MeleeWeaponComponent component) {
    }
}
