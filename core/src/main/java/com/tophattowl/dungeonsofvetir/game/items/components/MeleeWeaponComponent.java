package com.tophattowl.dungeonsofvetir.game.items.components;


import com.tophattowl.dungeonsofvetir.game.combat.DamageType;

public class MeleeWeaponComponent implements ItemComponent {
    public int damage;
    public DamageType damageType;

    public MeleeWeaponComponent(int damage, DamageType damageType) {
        this.damage = damage;
    }
}
