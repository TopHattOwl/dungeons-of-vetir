package com.tophattowl.dungeonsofvetir.game.dungeon.section;

/**
 * A playable flavor within a {@link SectionId}, variations of the same section
 * share a generator shape but differ in palette (maybe in params as well later)
 */
public enum SectionVariation {
    CAVES_DANK(SectionId.CAVES, "Dank Cave", TileTheme.CAVES_DANK),
    GROVE(SectionId.CAVES, "Mushroom Grove", TileTheme.GROVE),
    RUINS_FORTRESS(SectionId.RUINS, "Old Fortress", TileTheme.RUINS_FORTRESS);

    private final SectionId sectionId;
    private final String displayName;
    private final TileTheme theme;

    SectionVariation(SectionId sectionId, String displayName, TileTheme theme) {
        this.sectionId = sectionId;
        this.displayName = displayName;
        this.theme = theme;
    }

    public SectionId sectionId() {
        return sectionId;
    }

    public String displayName() {
        return displayName;
    }

    public TileTheme theme() {
        return theme;
    }
}
