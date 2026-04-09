package com.tophattowl.dungeonsofvetir.actors.body;

import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BodyComponentTest {

    @Test
    void getRandomBodyPart_ReturnsBodyPart() {
        List<BodyPart> parts = new ArrayList<>();
        parts.add(new BodyPart("head", BodyPartType.HEAD, com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole.VITAL,
            null, 100, 0, 0.1f, 0.1f, 1.2f));
        parts.add(new BodyPart("torso", BodyPartType.TORSO, com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f));
        
        BodyComponent body = new BodyComponent(parts);
        BodyPart random = body.getRandomBodyPart();
        
        assertNotNull(random);
        assertTrue(parts.contains(random));
    }

    @Test
    void getRandomBodyPart_UsesHitWeight() {
        List<BodyPart> parts = new ArrayList<>();
        parts.add(new BodyPart("head", BodyPartType.HEAD, com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole.VITAL,
            null, 100, 0, 0.1f, 0.9f, 1.2f));
        parts.add(new BodyPart("torso", BodyPartType.TORSO, com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.1f, 1.0f));
        
        BodyComponent body = new BodyComponent(parts);
        
        int headCount = 0;
        int torsoCount = 0;
        int iterations = 1000;
        
        for (int i = 0; i < iterations; i++) {
            BodyPart part = body.getRandomBodyPart();
            if (part.name.equals("head")) headCount++;
            if (part.name.equals("torso")) torsoCount++;
        }
        
        assertTrue(headCount > torsoCount, "Head should be selected more often due to higher hitWeight");
    }

    @Test
    void getPartByName_Existing() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        BodyPart head = body.getPartByName("head");
        
        assertNotNull(head);
        assertEquals("head", head.name);
        assertEquals(BodyPartType.HEAD, head.type);
    }

    @Test
    void getPartByName_NotExisting() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        BodyPart notFound = body.getPartByName("nonexistent");
        assertNull(notFound);
    }

    @Test
    void getPartByType_Existing() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        BodyPart arm = body.getPartByType(BodyPartType.ARM);
        
        assertNotNull(arm);
        assertEquals(BodyPartType.ARM, arm.type);
    }

    @Test
    void getPartByType_ReturnsOneOfMultiple() {
        List<BodyPart> parts = new ArrayList<>();
        parts.add(new BodyPart("Left Arm", BodyPartType.ARM, com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole.LIMB,
            null, 50, 0, 0.15f, 0.1f, 0.8f));
        parts.add(new BodyPart("Right Arm", BodyPartType.ARM, com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole.LIMB,
            null, 50, 0, 0.15f, 0.1f, 0.8f));
        
        BodyComponent body = new BodyComponent(parts);
        BodyPart arm = body.getPartByType(BodyPartType.ARM);
        
        assertNotNull(arm);
        assertEquals(BodyPartType.ARM, arm.type);
        assertTrue(arm.name.equals("Left Arm") || arm.name.equals("Right Arm"));
    }

    @Test
    void getPartByType_NotExisting_ThrowsException() {
        List<BodyPart> parts = new ArrayList<>();
        parts.add(new BodyPart("head", BodyPartType.HEAD, com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole.VITAL,
            null, 100, 0, 0.1f, 0.1f, 1.2f));
        
        BodyComponent body = new BodyComponent(parts);
        assertThrows(IllegalArgumentException.class, () -> body.getPartByType(BodyPartType.WING));
    }

    @Test
    void toString_ContainsAllParts() {
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        String str = body.toString();
        
        assertTrue(str.contains("head"));
        assertTrue(str.contains("torso"));
    }
}
