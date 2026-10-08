package com.tophattowl.dungeonsofvetir.game.world;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Capabilities denied by virtue of being in a place kind. Continuous/environmental
 * (checked, not stored on entities).
 */
public final class ZoneConstraints {

    private static final Map<PlaceKind, Set<Capability>> DENIED = new EnumMap<>(PlaceKind.class);

    static {
        DENIED.put(PlaceKind.SETTLEMENT, EnumSet.of(Capability.ATTACK, Capability.VISION));
        DENIED.put(PlaceKind.OVERWORLD, EnumSet.of(
            Capability.ATTACK, Capability.EQUIP, Capability.REST, Capability.VISION
        ));
    }

    private ZoneConstraints() {}

    public static boolean denies(PlaceKind kind, Capability capability) {
        Set<Capability> denied = DENIED.get(kind);
        return denied != null && denied.contains(capability);
    }
}
