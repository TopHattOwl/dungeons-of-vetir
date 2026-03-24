package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs;

import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;

public record MeleeWeaponSpec(int damage, DamageType damageType) implements ComponentSpec<MeleeWeaponComponent>{
    @Override
    public Class<MeleeWeaponComponent> getComponentType() {
        return MeleeWeaponComponent.class;
    }

    @Override
    public MeleeWeaponComponent build() {
        return new MeleeWeaponComponent(damage, damageType);
    }
}
