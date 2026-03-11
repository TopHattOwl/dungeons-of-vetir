package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;


public class OffensiveStatsComponent implements Component {

    // base damage of attack with no weapons equipped
    public int baseDamage;

    // modifies weapon damage
    public float weaponDamageModifier;

    // efficiency of main hand
    public float mainHandEfficiency;

    // efficiency of off hand(s)
    public float offHandEfficiencyModifier;

    // value above 1.0 reduces target dodge chance
    public float accuracy;


    public OffensiveStatsComponent(int baseDamage, float weaponDamageModifier,
                                   float mainHandEfficiency, float offHandEfficiencyModifier,
                                   float accuracy) {
        this.baseDamage = baseDamage;
        this.weaponDamageModifier = weaponDamageModifier;
        this.mainHandEfficiency = mainHandEfficiency;
        this.offHandEfficiencyModifier = offHandEfficiencyModifier;
        this.accuracy = accuracy;
    }
}
