package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlot;

import java.util.List;

public record BodyPartDefinition(
    String name,
    BodyPartType type,
    BodyPartRole role,
    float hpShare,
    float hitWeight,
    float damageMultiplier,
    List<EquipmentSlot> equippableSlots
) {
    // constructor for bodyguards with no equipment slot
    public BodyPartDefinition(String name, BodyPartType type, BodyPartRole role,
                              float hpShare, float hitChance, float hitMultiplier) {
        this(name, type, role, hpShare, hitChance, hitMultiplier,null);
    }
}
