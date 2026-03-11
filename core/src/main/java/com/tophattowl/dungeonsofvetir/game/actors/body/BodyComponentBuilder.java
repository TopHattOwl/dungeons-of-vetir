package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a BodyComponent from a template
 * Distributes hp to segments, min hp customizable
 */
public class BodyComponentBuilder {
    private static final int MIN_PART_HP = 5;

    public static BodyComponent build(BodyTemplate bodyTemplate, int maxHp) {
        List<BodyPart> parts = new ArrayList<>();

        for (BodyPartDefinition def : bodyTemplate.parts) {
            int partHp = Math.max(MIN_PART_HP, Math.round(maxHp * def.hpShare()));
            BodyPart part = new BodyPart(
                def.name(), def.type(), def.role(),
                def.equippableSlots(), partHp, def.hpShare(), def.hitChance()
            );
            parts.add(part);
        }
        return new BodyComponent(parts);
    }
}
