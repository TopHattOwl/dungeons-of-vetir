package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlot;

import java.util.List;

public record BodyPartDefinition(
    String name,
    BodyPartType type,
    BodyPartRole role,
    float hpShare,
    float hitChance,
    List<EquipmentSlot> equippableSlots
) {
    // constructor for bodyguards with no equipment slot
    public BodyPartDefinition(String name, BodyPartType type, BodyPartRole role,
                              float hpShare, float hitChance) {
        this(name, type, role, hpShare, hitChance, null);
    }
}
