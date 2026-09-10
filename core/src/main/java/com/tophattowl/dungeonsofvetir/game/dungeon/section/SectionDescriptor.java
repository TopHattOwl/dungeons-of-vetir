package com.tophattowl.dungeonsofvetir.game.dungeon.section;

import java.util.List;

/**
 * Configuration for a section,
 * Floor spans are derived by
 * {@link SectionCatalog}/{@link WorldLayout}
 */
public record SectionDescriptor(
    SectionId id,
    String name,
    int length,
    List<SectionVariation> variations
) {
    public SectionDescriptor {
        if (length < 1) throw new IllegalArgumentException("Section length must be >= 1");
        if (variations.isEmpty()) throw new IllegalArgumentException("Section must have at least one variation");
        variations = List.copyOf(variations);
    }
}
