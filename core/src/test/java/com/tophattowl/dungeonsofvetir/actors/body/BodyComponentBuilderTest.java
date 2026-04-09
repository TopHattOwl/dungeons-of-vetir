package com.tophattowl.dungeonsofvetir.actors.body;

import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

class BodyComponentBuilderTest {

    @Test
    void build_Humanoid_Creates6Parts() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        assertEquals(6, body.bodyParts.size());
    }

    @Test
    void build_Humanoid_HasCorrectParts() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        
        assertNotNull(body.getPartByName("head"));
        assertNotNull(body.getPartByName("torso"));
        assertNotNull(body.getPartByName("Left Arm"));
        assertNotNull(body.getPartByName("Right Arm"));
        assertNotNull(body.getPartByName("Left leg"));
        assertNotNull(body.getPartByName("Right leg"));
    }

    @Test
    void build_Humanoid_HasEquipmentSlots() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        BodyPart head = body.getPartByName("head");
        
        assertNotNull(head.equippableSlots);
        assertFalse(head.equippableSlots.isEmpty());
    }

    @Test
    void build_Worm_Creates5Parts() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.WORM, 120, null);
        assertEquals(5, body.bodyParts.size());
    }

    @Test
    void build_Quadruped_Creates6Parts() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.QUADRUPED, 200, null);
        assertEquals(6, body.bodyParts.size());
    }

    @Test
    void build_Arachnid_Creates10Parts() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.ARACHNID, 300, null);
        assertEquals(10, body.bodyParts.size());
    }

    @Test
    void build_MinPartHp_Enforced() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 10, null);
        
        for (BodyPart part : body.bodyParts) {
            assertTrue(part.maxHp >= 5, "Part " + part.name + " should have at least 5 HP");
        }
    }

    @Test
    void build_HpShareDistribution() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        
        BodyPart head = body.getPartByName("head");
        assertTrue(head.maxHp >= 10);
    }

    @Test
    void build_NaturalProtections_Applied() {
        Map<String, Integer> protections = new HashMap<>();
        protections.put("head", 10);
        protections.put("torso", 15);
        
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, protections);
        
        assertEquals(10, body.getPartByName("head").naturalProtection);
        assertEquals(15, body.getPartByName("torso").naturalProtection);
    }

    @Test
    void build_NullProtections_DefaultsToZero() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        
        for (BodyPart part : body.bodyParts) {
            assertEquals(0, part.naturalProtection);
        }
    }

    @Test
    void build_Humanoid_HeadIsVital() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        BodyPart head = body.getPartByName("head");
        
        assertEquals(BodyPartRole.VITAL, head.role);
        assertTrue(head.isVital());
    }

    @Test
    void build_Humanoid_TorsoIsVital() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        BodyPart torso = body.getPartByName("torso");
        
        assertEquals(BodyPartRole.VITAL, torso.role);
        assertTrue(torso.isVital());
    }

    @Test
    void build_Humanoid_ArmsAreLimbs() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        BodyPart leftArm = body.getPartByName("Left Arm");
        BodyPart rightArm = body.getPartByName("Right Arm");
        
        assertEquals(BodyPartRole.LIMB, leftArm.role);
        assertEquals(BodyPartRole.LIMB, rightArm.role);
        assertFalse(leftArm.isVital());
        assertFalse(rightArm.isVital());
    }

    @Test
    void build_Humanoid_LegsAreLimbs() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        BodyPart leftLeg = body.getPartByName("Left leg");
        BodyPart rightLeg = body.getPartByName("Right leg");
        
        assertEquals(BodyPartRole.LIMB, leftLeg.role);
        assertEquals(BodyPartRole.LIMB, rightLeg.role);
        assertFalse(leftLeg.isVital());
        assertFalse(rightLeg.isVital());
    }

    @Test
    void build_Worm_TailIsAppendage() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.WORM, 120, null);
        BodyPart tail = body.getPartByName("tail");
        
        assertEquals(BodyPartRole.APPENDAGE, tail.role);
        assertFalse(tail.isVital());
    }

    @Test
    void build_HitWeightsSum_IsCalculatedCorrectly() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        
        float totalWeight = 0f;
        for (BodyPart part : body.bodyParts) {
            totalWeight += part.hitWeight;
        }
        
        assertEquals(0.8f, totalWeight, 0.001f, "Hit weights sum to 0.8 for HUMANOID");
    }

    @Test
    void build_HpSharesSum_ApproximatelyOne() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        
        float totalShare = 0f;
        for (BodyPart part : body.bodyParts) {
            totalShare += part.hpShare;
        }
        
        assertEquals(1.0f, totalShare, 0.001f, "HP shares should sum to 1.0");
    }
}
