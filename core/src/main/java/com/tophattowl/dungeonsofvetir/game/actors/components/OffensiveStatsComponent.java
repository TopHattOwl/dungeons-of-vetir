package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;


public class OffensiveStatsComponent implements Component {

    // base damage of the attack with no weapons equipped
    public int baseDamage;

    // modifies weapon damage
    public float weaponDamageModifier;

    // efficiency of main hand slot
    public float mainHandEfficiency;

    // efficiency of hand slots that are not main hand
    public float offHandEfficiencyModifier;

    public int accuracy;


    public OffensiveStatsComponent(int baseDamage, float weaponDamageModifier,
                                   float mainHandEfficiency, float offHandEfficiencyModifier,
                                   int accuracy) {
        this.baseDamage = baseDamage;
        this.weaponDamageModifier = weaponDamageModifier;
        this.mainHandEfficiency = mainHandEfficiency;
        this.offHandEfficiencyModifier = offHandEfficiencyModifier;
        this.accuracy = accuracy;
    }
}
