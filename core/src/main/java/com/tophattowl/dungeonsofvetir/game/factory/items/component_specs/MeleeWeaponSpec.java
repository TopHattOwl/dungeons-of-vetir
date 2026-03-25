package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs;

import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.helpers.ElementDamage;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;

public record MeleeWeaponSpec(
    DamageType damageType,
    boolean isTwoHanded,
    ElementDamage... damages
) implements ComponentSpec<MeleeWeaponComponent>{

    public MeleeWeaponSpec(DamageType damageType, ElementDamage... damages) {
        this(damageType, false, damages);
    }

    @Override
    public Class<MeleeWeaponComponent> getComponentType() {
        return MeleeWeaponComponent.class;
    }

    @Override
    public MeleeWeaponComponent build() {
        MeleeWeaponComponent comp = new MeleeWeaponComponent(damageType, isTwoHanded);

        for (ElementDamage damage : damages) {
            comp.addDamageInstance(damage.elementType(), damage.amount());
        }
        return comp;
    }
}
