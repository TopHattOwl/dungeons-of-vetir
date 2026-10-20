package com.tophattowl.dungeonsofvetir.world;

import com.tophattowl.dungeonsofvetir.game.generation.HashUtil;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.Constraint;
import com.tophattowl.dungeonsofvetir.game.world.ConstraintComponent;
import com.tophattowl.dungeonsofvetir.game.world.PlaceKind;
import com.tophattowl.dungeonsofvetir.game.world.ZoneConstraints;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapabilitiesTest {

    @Test
    void compiledConstraints_denyTheirCapability() {
        ConstraintComponent component = new ConstraintComponent();
        component.setAll(List.of(Constraint.of(Capability.ATTACK, "stunned")));

        assertFalse(component.allows(Capability.ATTACK));
        assertTrue(component.allows(Capability.MOVE));
        assertFalse(component.isEmpty());
    }

    @Test
    void clear_restoresAllCapabilities() {
        ConstraintComponent component = new ConstraintComponent();
        component.setAll(List.of(Constraint.of(Capability.ATTACK, "stunned")));
        component.clear();

        assertTrue(component.allows(Capability.ATTACK));
        assertTrue(component.isEmpty());
    }

    @Test
    void setAll_replacesPreviousSet() {
        ConstraintComponent component = new ConstraintComponent();
        component.setAll(List.of(Constraint.of(Capability.ATTACK, "stunned")));
        component.setAll(List.of(Constraint.of(Capability.REST, "wounded")));

        assertTrue(component.allows(Capability.ATTACK));
        assertFalse(component.allows(Capability.REST));
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
