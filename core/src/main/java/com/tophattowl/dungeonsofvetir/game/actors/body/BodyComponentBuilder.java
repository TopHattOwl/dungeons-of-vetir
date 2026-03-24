package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builds a BodyComponent from a template
 * Distributes hp to segments, min hp customizable
 */
public class BodyComponentBuilder {
    private static final int MIN_PART_HP = 5;

    public static BodyComponent build(BodyTemplate bodyTemplate, int maxHp,
                                      Map<String, Integer> naturalProtections) {
        List<BodyPart> parts = new ArrayList<>();

        for (BodyPartDefinition def : bodyTemplate.parts) {
            int partHp = Math.max(MIN_PART_HP, Math.round(maxHp * def.hpShare()));

            int naturalProt = 0;
            if (naturalProtections != null) {
                if (naturalProtections.containsKey(def.name())) {
                    naturalProt = naturalProtections.get(def.name());
                }
            }

            BodyPart part = new BodyPart(
                def.name(), def.type(), def.role(),
                def.equippableSlots(), partHp, naturalProt, def.hpShare(), def.hitWeight(), def.damageMultiplier()
            );
            parts.add(part);
        }
        return new BodyComponent(parts);
    }
}
