package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;

import java.util.List;

public record BodyPartDefinition(
    String name,
    BodyPartType type,
    BodyPartRole role,
    float hpShare,
    float hitWeight,
    float damageMultiplier,
    List<EquipmentSlotType> equippableSlots
    ) {

    // constructor for bodyguards with no equipment slot
    public BodyPartDefinition(String name, BodyPartType type, BodyPartRole role,
                              float hpShare, float hitWeight, float damageMultiplier) {
        this(name, type, role, hpShare, hitWeight, damageMultiplier,null);
    }
}
