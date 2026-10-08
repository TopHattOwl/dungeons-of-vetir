package com.tophattowl.dungeonsofvetir.game.dungeon.section;

import com.tophattowl.dungeonsofvetir.game.world.PlaceRole;
import com.tophattowl.dungeonsofvetir.game.world.TileTheme;

/**
 * The fully resolved plan for a single floor
 */
public record ResolvedFloor(
    WorldSection section,
    SectionVariation variation,
    PlaceRole role,
    int floorNumber,
    long seed
) {
    /**
     * Palette for this floor,
     * Rest floors get their own theme, others use variations
     */
    public TileTheme theme() {
        return role == PlaceRole.REST ? TileTheme.REST : variation.theme();
    }
}
