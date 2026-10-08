package com.tophattowl.dungeonsofvetir.world;

import com.tophattowl.dungeonsofvetir.game.generation.HashUtil;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.Constraint;
import com.tophattowl.dungeonsofvetir.game.world.ConstraintComponent;
import com.tophattowl.dungeonsofvetir.game.world.PlaceKind;
import com.tophattowl.dungeonsofvetir.game.world.ZoneConstraints;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapabilitiesTest {

    @Test
    void timedConstraint_expiresAfterItsDuration() {
        ConstraintComponent component = new ConstraintComponent();
        component.add(Constraint.forTurns(Capability.REST, "wounded", 2));

        assertFalse(component.allows(Capability.REST));
        component.tick();
        assertFalse(component.allows(Capability.REST));
        component.tick();
        assertTrue(component.allows(Capability.REST), "should expire after 2 turns");
    }

    @Test
    void permanentConstraint_neverExpires() {
        ConstraintComponent component = new ConstraintComponent();
        component.add(Constraint.permanent(Capability.ATTACK, "curse"));

        for (int i = 0; i < 10; i++) component.tick();
        assertFalse(component.allows(Capability.ATTACK));
    }

    @Test
    void zoneConstraints_denyByPlaceKind() {
        assertTrue(ZoneConstraints.denies(PlaceKind.OVERWORLD, Capability.ATTACK));
        assertTrue(ZoneConstraints.denies(PlaceKind.OVERWORLD, Capability.EQUIP));
        assertTrue(ZoneConstraints.denies(PlaceKind.OVERWORLD, Capability.VISION));
        assertTrue(ZoneConstraints.denies(PlaceKind.SETTLEMENT, Capability.ATTACK));

        assertFalse(ZoneConstraints.denies(PlaceKind.DUNGEON_FLOOR, Capability.ATTACK));
        assertFalse(ZoneConstraints.denies(PlaceKind.DUNGEON_FLOOR, Capability.VISION));
        assertFalse(ZoneConstraints.denies(PlaceKind.OVERWORLD, Capability.MOVE));
    }

    @Test
    void hashUtil_isStableAndDiscriminating() {
        assertEquals(HashUtil.mix(1L, "abc"), HashUtil.mix(1L, "abc"));
        assertEquals(HashUtil.mix(HashUtil.mix(1L, 3), 7), HashUtil.mix(HashUtil.mix(1L, 3), 7));
        assertNotEquals(HashUtil.mix(1L, "abc"), HashUtil.mix(2L, "abc"));
        assertNotEquals(HashUtil.mix(1L, "abc"), HashUtil.mix(1L, "abd"));
    }
}
