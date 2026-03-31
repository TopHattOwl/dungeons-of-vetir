package com.tophattowl.dungeonsofvetir.game.factory.items.component_specs;

import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.factory.items.component_specs.helpers.ElementDamage;
import com.tophattowl.dungeonsofvetir.game.items.ItemGripType;
import com.tophattowl.dungeonsofvetir.game.items.components.MeleeWeaponComponent;

public record MeleeWeaponSpec(
    DamageType damageType,
    ItemGripType gripType,
    ElementDamage... damages
) implements ItemComponentSpec<MeleeWeaponComponent> {

    @Override
    public Class<MeleeWeaponComponent> getComponentType() {
        return MeleeWeaponComponent.class;
    }

    @Override
    public MeleeWeaponComponent build() {
        MeleeWeaponComponent comp = new MeleeWeaponComponent(damageType, gripType);

        for (ElementDamage damage : damages) {
            comp.addDamageInstance(damage.elementType(), damage.amount());
        }
        return comp;
    }
}
