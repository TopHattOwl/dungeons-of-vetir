package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class DefensiveStatsComponent implements Component {
    public Map<BodyPart, Integer> protections = new HashMap<>();
    public EnumMap<ElementType, Float> resistances = new EnumMap<>(ElementType.class);
    public int evasion;
    public float counterChance;
    public float blockChance;

    public DefensiveStatsComponent(int evasion, float counterChance, float blockChance, BodyComponent bodyComp) {
        this.evasion = evasion;
        this.counterChance = counterChance;
        this.blockChance = blockChance;
        initResistances();
        initProtections(bodyComp);

    }

    private void initResistances() {
        for (ElementType elementType : ElementType.values()) {
            resistances.put(elementType, 0.0f);
        }
    }

    private void initProtections(BodyComponent bodyComp) {
        for (BodyPart bodypart : bodyComp.bodyParts) {
            protections.put(bodypart, 0);
        }
    }
}
