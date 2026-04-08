package com.tophattowl.dungeonsofvetir.game.items.components;


import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageInstance;
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageProfile;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;

import java.util.EnumMap;
import java.util.Map;

public class MeleeWeaponComponent implements ItemComponent, DamageProfile {
    private Map<ElementType, DamageInstance> damages = new EnumMap<>(ElementType.class);
    private DamageType damageType;
    private ItemGripType gripType;

    public MeleeWeaponComponent(DamageType damageType, ItemGripType gripType) {
        this.damageType = damageType;
        this.gripType = gripType;
    }

    public void addDamageInstance(ElementType elementType, int damage) {
        damages.put(elementType, new DamageInstance(damage, elementType));
    }

    @Override
    public Map<ElementType, DamageInstance> getDamages() {
        return damages;
    }

    @Override
    public DamageType getDamageType() {
        return damageType;
    }

    public ItemGripType getGripType() {
        return gripType;
    }
}
