package com.tophattowl.dungeonsofvetir.game.actors.monsters;

import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageInstance;
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageProfile;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class NaturalWeapon implements DamageProfile {
    private Map<ElementType, DamageInstance> damages = new EnumMap<>(ElementType.class);
    private DamageType damageType;
    private float attackChance;

    public NaturalWeapon(DamageType damageType, float attackChance, Map<ElementType, Integer> damages) {
        this.damageType = damageType;
        this.attackChance = attackChance;

        for (Map.Entry<ElementType, Integer> entry : damages.entrySet()) {
            this.damages.put(entry.getKey(), new DamageInstance(entry.getValue(), entry.getKey()));
        }
    }

    @Override
    public Map<ElementType, DamageInstance> getDamages() {
        return damages;
    }

    @Override
    public DamageType getDamageType() {
        return damageType;
    }

    public float getAttackChance() {
        return attackChance;
    }
}
