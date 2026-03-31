package com.tophattowl.dungeonsofvetir.game.items.components;


import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageInstance;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;

import java.util.EnumMap;
import java.util.Map;

public class MeleeWeaponComponent implements ItemComponent {
    public Map<ElementType, DamageInstance> damages = new EnumMap<>(ElementType.class);
    public DamageType damageType;
    public ItemGripType gripType;

    public MeleeWeaponComponent(DamageType damageType, ItemGripType gripType) {
        this.damageType = damageType;
        this.gripType = gripType;
    }

    public void addDamageInstance(ElementType elementType, int damage) {
        damages.put(elementType, new DamageInstance(damage, elementType));
    }
}
