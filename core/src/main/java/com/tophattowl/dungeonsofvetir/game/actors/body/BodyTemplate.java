package com.tophattowl.dungeonsofvetir.game.actors.body;

import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlot;

import java.util.List;

/**
 * Rules:
 * - hitChance values must add up yo 1.0
 * - hpShare values must add up to 1.0
 */
public enum BodyTemplate {
    HUMANOID(List.of(
        new BodyPartDefinition("head", BodyPartType.HEAD, BodyPartRole.VITAL, 0.1f, 0.1f,
            List.of(EquipmentSlot.HEAD, EquipmentSlot.HEAD_UNDER)),
        new BodyPartDefinition("torso", BodyPartType.TORSO, BodyPartRole.VITAL, 0.3f, 0.3f,
            List.of(EquipmentSlot.CHEST, EquipmentSlot.CHEST_UNDER)),
        new BodyPartDefinition("Left Arm", BodyPartType.ARM, BodyPartRole.LIMB, 0.15f, 0.1f,
            List.of(EquipmentSlot.ARM, EquipmentSlot.HAND, EquipmentSlot.OFF_HAND)),
        new BodyPartDefinition("Right Arm", BodyPartType.ARM, BodyPartRole.LIMB, 0.15f, 0.1f,
            List.of(EquipmentSlot.ARM, EquipmentSlot.HAND, EquipmentSlot.MAIN_HAND)),
        new BodyPartDefinition("Left leg", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f,
            List.of(EquipmentSlot.LEG, EquipmentSlot.FOOT)),
        new BodyPartDefinition("Right leg", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f,
            List.of(EquipmentSlot.LEG, EquipmentSlot.FOOT))

    )),
    QUADRUPED(List.of(
        new BodyPartDefinition("head", BodyPartType.HEAD, BodyPartRole.VITAL, 0.1f, 0.1f,
            List.of(EquipmentSlot.HEAD, EquipmentSlot.HEAD_UNDER)),
        new BodyPartDefinition("torso", BodyPartType.TORSO, BodyPartRole.VITAL, 0.3f, 0.3f,
            List.of(EquipmentSlot.CHEST, EquipmentSlot.CHEST_UNDER)),
        new BodyPartDefinition("Front left leg", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f,
            List.of(EquipmentSlot.LEG, EquipmentSlot.FOOT)),
        new BodyPartDefinition("Front right leg", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f,
            List.of(EquipmentSlot.LEG, EquipmentSlot.FOOT)),
        new BodyPartDefinition("Hind left leg", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f,
            List.of(EquipmentSlot.LEG, EquipmentSlot.FOOT)),
        new BodyPartDefinition("Hind right leg", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f,
            List.of(EquipmentSlot.LEG, EquipmentSlot.FOOT))
    )),
    ARACHNID(List.of(
        new BodyPartDefinition("head", BodyPartType.HEAD, BodyPartRole.VITAL, 0.1f, 0.1f),
        new BodyPartDefinition("thorax", BodyPartType.THORAX, BodyPartRole.VITAL, 0.1f, 0.1f),
        new BodyPartDefinition("Leg 1", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f),
        new BodyPartDefinition("Leg 2", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f),
        new BodyPartDefinition("Leg 3", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f),
        new BodyPartDefinition("Leg 4", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f),
        new BodyPartDefinition("Leg 5", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f),
        new BodyPartDefinition("Leg 6", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f),
        new BodyPartDefinition("Leg 7", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f),
        new BodyPartDefinition("Leg 8", BodyPartType.LEG, BodyPartRole.LIMB, 0.15f, 0.1f)
    )),
//    SNAKE,
    WORM(List.of(
        new BodyPartDefinition("head", BodyPartType.HEAD, BodyPartRole.VITAL, 0.15f, 0.15f),
        new BodyPartDefinition("segment1", BodyPartType.SEGMENT, BodyPartRole.LIMB, 0.2f, 0.2f),
        new BodyPartDefinition("segment2", BodyPartType.SEGMENT, BodyPartRole.LIMB, 0.2f, 0.2f),
        new BodyPartDefinition("segment3", BodyPartType.SEGMENT, BodyPartRole.LIMB, 0.2f, 0.2f),
        new BodyPartDefinition("tail", BodyPartType.TAIL, BodyPartRole.APPENDAGE, 0.15f, 0.15f)
    ))
    ;

    public final List<BodyPartDefinition> parts;

    BodyTemplate(List<BodyPartDefinition> parts) {
        this.parts = parts;
    }
}
