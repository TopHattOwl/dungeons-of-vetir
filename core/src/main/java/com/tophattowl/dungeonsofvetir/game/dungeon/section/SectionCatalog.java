package com.tophattowl.dungeonsofvetir.game.dungeon.section;

import java.util.List;

/**
 * The ordered list of sections that make up a run
 * <p>
 * Adding a section = append one {@link SectionDescriptor} here (and make sure a
 * {@code LevelGenerator} is registered for its {@link SectionId}).
 */
public class SectionCatalog {

    private SectionCatalog() {}

    public static List<SectionDescriptor> defaultCatalog() {
        return List.of(
            new SectionDescriptor(
                SectionId.CAVES,
                "Caves",
                5,
                List.of(SectionVariation.CAVES_DANK, SectionVariation.GROVE)
            ),
            new SectionDescriptor(
                SectionId.RUINS,
                "Ruins",
                5,
                List.of(SectionVariation.RUINS_FORTRESS)
            )
        );
    }
}
