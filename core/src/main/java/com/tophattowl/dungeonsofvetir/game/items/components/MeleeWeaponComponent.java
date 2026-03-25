package com.tophattowl.dungeonsofvetir.game.items.components;


import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageInstance;

import java.util.EnumMap;
import java.util.Map;

public class MeleeWeaponComponent implements ItemComponent {
    public Map<ElementType, DamageInstance> damages = new EnumMap<>(ElementType.class);
    public DamageType damageType;
    public boolean isTwoHanded;

    public MeleeWeaponComponent(DamageType damageType, boolean isTwoHanded) {
        this.damageType = damageType;
        this.isTwoHanded = isTwoHanded;
    }

    public void addDamageInstance(ElementType elementType, int damage) {
        damages.put(elementType, new DamageInstance(damage, elementType));
    }
}
