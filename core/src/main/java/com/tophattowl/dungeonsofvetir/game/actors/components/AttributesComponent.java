package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.stats.Attribute;

import java.util.EnumMap;
import java.util.Map;

/**
 * An entity's attributes
 * <p>
 * {@code base} is the innate value <br>
 * {@code effective} is base plus modifiers and is written by the {@code StatAggregator} <br>
 * Checks (forcing doors, swimming) read {@link #effective} value
 */
public class AttributesComponent implements Component {

    private final EnumMap<Attribute, Integer> base = new EnumMap<>(Attribute.class);
    private final EnumMap<Attribute, Float> effective = new EnumMap<>(Attribute.class);

    public AttributesComponent(int thew, int wind, int flesh, int blood, int grit) {
        base.put(Attribute.THEW, thew);
        base.put(Attribute.WIND, wind);
        base.put(Attribute.FLESH, flesh);
        base.put(Attribute.BLOOD, blood);
        base.put(Attribute.GRIT, grit);
        effective.putAll(toFloats());
    }

    public int base(Attribute attribute) {
        return base.getOrDefault(attribute, 0);
    }

    public float effective(Attribute attribute) {
        return effective.getOrDefault(attribute, 0f);
    }

    public void setEffective(Attribute attribute, float value) {
        effective.put(attribute, value);
    }

    public Map<Attribute, Integer> baseValues() {
        return base;
    }

    private EnumMap<Attribute, Float> toFloats() {
        EnumMap<Attribute, Float> floats = new EnumMap<>(Attribute.class);
        base.forEach((k, v) -> floats.put(k, (float) v));
        return floats;
    }
}
