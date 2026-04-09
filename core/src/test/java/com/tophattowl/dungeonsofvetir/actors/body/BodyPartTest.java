package com.tophattowl.dungeonsofvetir.actors.body;

import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartStatus;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BodyPartTest {

    @Test
    void constructor_SetsAllFields() {
        List<EquipmentSlotType> slots = List.of(EquipmentSlotType.HEAD);
        BodyPart part = new BodyPart("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            slots, 50, 10, 0.1f, 0.15f, 1.2f);

        assertEquals("head", part.name);
        assertEquals(BodyPartType.HEAD, part.type);
        assertEquals(BodyPartRole.VITAL, part.role);
        assertEquals(50, part.maxHp);
        assertEquals(10, part.naturalProtection);
        assertEquals(0.1f, part.hpShare);
        assertEquals(0.15f, part.hitWeight);
        assertEquals(1.2f, part.damageMultiplier);
        assertEquals(slots, part.equippableSlots);
    }

    @Test
    void constructor_InitializesHpToMax() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        assertEquals(100, part.hp);
        assertEquals(100, part.maxHp);
    }

    @Test
    void constructor_InitializesStatusHealthy() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        assertEquals(BodyPartStatus.HEALTHY, part.status);
    }

    @Test
    void isDestroyed_True_WhenDestroyed() {
        BodyPart part = new BodyPart("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            null, 100, 0, 0.1f, 0.1f, 1.2f);
        part.hp = 0;
        part.status = BodyPartStatus.DESTROYED;
        assertTrue(part.isDestroyed());
    }

    @Test
    void isDestroyed_False_WhenNotDestroyed() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        part.status = BodyPartStatus.HEALTHY;
        assertFalse(part.isDestroyed());
    }

    @Test
    void isVital_True_ForVitalRole() {
        BodyPart part = new BodyPart("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            null, 100, 0, 0.1f, 0.1f, 1.2f);
        assertTrue(part.isVital());
    }

    @Test
    void isVital_False_ForLimbRole() {
        BodyPart part = new BodyPart("arm", BodyPartType.ARM, BodyPartRole.LIMB,
            null, 50, 0, 0.15f, 0.1f, 0.8f);
        assertFalse(part.isVital());
    }

    @Test
    void isVital_False_ForAppendageRole() {
        BodyPart part = new BodyPart("tail", BodyPartType.TAIL, BodyPartRole.APPENDAGE,
            null, 40, 0, 0.15f, 0.15f, 0.8f);
        assertFalse(part.isVital());
    }

    @Test
    void takeDamage_AppliesDamageMultiplier() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        int initialHp = part.hp;
        part.takeDamage(10);
        assertEquals(initialHp - 4, part.hp);
    }

    @Test
    void takeDamage_ReturnsFalse_WhenNotDestroyed() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        boolean result = part.takeDamage(10);
        assertFalse(result);
    }

    @Test
    void takeDamage_ReturnsTrue_WhenDestroyed() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        boolean result = part.takeDamage(250);
        assertTrue(result);
        assertEquals(0, part.hp);
    }

    @Test
    void takeDamage_FloorsAtZero() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        part.takeDamage(1000);
        assertEquals(0, part.hp);
    }

    @Test
    void takeDamage_UpdatesStatusHealthy() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        part.takeDamage(30);
        assertEquals(BodyPartStatus.HEALTHY, part.status);
    }

    @Test
    void takeDamage_UpdatesStatusInjured() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        part.takeDamage(125);
        assertEquals(BodyPartStatus.INJURED, part.status);
    }

    @Test
    void takeDamage_UpdatesStatusCrippled() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        part.takeDamage(200);
        assertEquals(BodyPartStatus.CRIPPLED, part.status);
    }

    @Test
    void takeDamage_UpdatesStatusDestroyed() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        part.takeDamage(250);
        assertEquals(BodyPartStatus.DESTROYED, part.status);
    }

    @Test
    void takeDamage_StatusBoundary_At60Percent() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        int damageTo60Percent = (int) (100 * (1 - 0.6) / 0.4);
        part.takeDamage(damageTo60Percent);
        assertEquals(BodyPartStatus.INJURED, part.status);
    }

    @Test
    void takeDamage_StatusBoundary_At30Percent() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        int damageTo30Percent = (int) ((1 - 0.3) / 0.4 * 100);
        part.takeDamage(damageTo30Percent);
        assertEquals(BodyPartStatus.CRIPPLED, part.status);
    }

    @Test
    void multipleDamageApplications() {
        BodyPart part = new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, 100, 0, 0.3f, 0.3f, 1.0f);
        part.takeDamage(10);
        part.takeDamage(10);
        part.takeDamage(10);
        assertEquals(88, part.hp);
    }
}
