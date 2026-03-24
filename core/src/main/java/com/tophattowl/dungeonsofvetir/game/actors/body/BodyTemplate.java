package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;

import java.util.List;

/**
 * Rules:
 * - hpShare values must add up to 1.0
 */
public enum BodyTemplate {
    HUMANOID(List.of(
        new BodyPartDefinition("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            0.1f, 0.1f, 1.2f,
            List.of(EquipmentSlotType.HEAD, EquipmentSlotType.HEAD_UNDER)),

        new BodyPartDefinition("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            0.3f, 0.3f, 1.0f,
            List.of(EquipmentSlotType.CHEST, EquipmentSlotType.CHEST_UNDER)),

        new BodyPartDefinition("Left Arm", BodyPartType.ARM, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f,
            List.of(EquipmentSlotType.ARM, EquipmentSlotType.HAND, EquipmentSlotType.HAND_SLOT)),

        new BodyPartDefinition("Right Arm", BodyPartType.ARM, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f,
            List.of(EquipmentSlotType.ARM, EquipmentSlotType.HAND, EquipmentSlotType.HAND_SLOT)),

        new BodyPartDefinition("Left leg", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f,
            List.of(EquipmentSlotType.LEG, EquipmentSlotType.FOOT)),

        new BodyPartDefinition("Right leg", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f,
            List.of(EquipmentSlotType.LEG, EquipmentSlotType.FOOT))

    )),
    QUADRUPED(List.of(
        new BodyPartDefinition("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            0.1f, 0.1f, 1.2f),
        new BodyPartDefinition("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            0.3f, 0.3f, 1.0f),
        new BodyPartDefinition("Front left leg", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Front right leg", BodyPartType.LEG, BodyPartRole.LIMB,
             0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Hind left leg", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Hind right leg", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f)
    )),
    ARACHNID(List.of(
        new BodyPartDefinition("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            0.1f, 0.1f, 1.2f),
        new BodyPartDefinition("thorax", BodyPartType.THORAX, BodyPartRole.VITAL,
            0.1f, 0.1f, 1.0f),
        new BodyPartDefinition("Leg 1", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Leg 2", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Leg 3", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Leg 4", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Leg 5", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Leg 6", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Leg 7", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f),
        new BodyPartDefinition("Leg 8", BodyPartType.LEG, BodyPartRole.LIMB,
            0.15f, 0.1f, 0.8f)
    )),
    WORM(List.of(
        new BodyPartDefinition("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            0.15f, 0.15f, 1.2f),
        new BodyPartDefinition("segment1", BodyPartType.SEGMENT, BodyPartRole.LIMB,
            0.2f, 0.2f, 1.0f),
        new BodyPartDefinition("segment2", BodyPartType.SEGMENT, BodyPartRole.LIMB,
            0.2f, 0.2f, 1.0f),
        new BodyPartDefinition("segment3", BodyPartType.SEGMENT, BodyPartRole.LIMB,
        0.2f, 0.2f, 1.0f),
        new BodyPartDefinition("tail", BodyPartType.TAIL, BodyPartRole.APPENDAGE,
            0.15f, 0.15f, 0.8f)
    ))
    ;

    public final List<BodyPartDefinition> parts;

    BodyTemplate(List<BodyPartDefinition> parts) {
        this.parts = parts;
    }
}
