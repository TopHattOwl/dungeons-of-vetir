package com.tophattowl.dungeonsofvetir.game.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionDescriptor;
import com.tophattowl.dungeonsofvetir.game.generation.GeneratorId;
import com.tophattowl.dungeonsofvetir.game.world.TileTheme;

import java.util.List;

/**
 * Static content for one dungeon: identity, name, its ordered floor sections, and
 * how its surface/entrance level is generated. Floors use each section's
 * {@link GeneratorId}; the surface is authored for named dungeons or procedural.
 */
public record DungeonDefinition(
    String key,
    String name,
    List<SectionDescriptor> sections,
    String surfaceKey,
    GeneratorId surfaceGenerator,
    TileTheme surfaceTheme
) {
    public DungeonDefinition {
        sections = List.copyOf(sections);
    }
}
