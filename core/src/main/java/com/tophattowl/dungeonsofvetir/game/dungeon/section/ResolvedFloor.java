package com.tophattowl.dungeonsofvetir.game.dungeon.section;

/**
 * The fully resolved plan for a single floor:
 * which section/variation it belongs to,
 * its role, and the deterministic seed used to generate it
 */
public record ResolvedFloor(
    WorldSection section,
    SectionVariation variation,
    FloorRole role,
    int floorNumber,
    long seed
) {
    /**
     * Palette for this floor,
     * Rest floors get their own theme, others use variations
     */
    public TileTheme theme() {
        return role == FloorRole.REST ? TileTheme.REST : variation.theme();
    }
}
