package com.tophattowl.dungeonsofvetir.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.section.FloorRole;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.ResolvedFloor;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionCatalog;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionId;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionVariation;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.WorldLayout;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldLayoutTest {

    private static final long SEED = 999L;

    private static WorldLayout layout() {
        return new WorldLayout(SEED, SectionCatalog.defaultCatalog());
    }

    @Test
    void resolve_FirstFloor_IsFirstSectionNormal() {
        ResolvedFloor resolved = layout().resolve(1);
        assertEquals(SectionId.CAVES, resolved.section().id());
        assertEquals(FloorRole.NORMAL, resolved.role());
    }

    @Test
    void resolve_VariationConstantWithinSection() {
        WorldLayout layout = layout();
        SectionVariation first = layout.resolve(1).variation();
        for (int floor = 1; floor <= 5; floor++) {
            assertEquals(first, layout.resolve(floor).variation(),
                "All floors of a section should share the same variation");
        }
    }

    @Test
    void resolve_VariationDeterministicForSeed() {
        WorldLayout a = new WorldLayout(SEED, SectionCatalog.defaultCatalog());
        WorldLayout b = new WorldLayout(SEED, SectionCatalog.defaultCatalog());
        assertEquals(a.resolve(1).variation(), b.resolve(1).variation());
        assertEquals(a.resolve(7).variation(), b.resolve(7).variation());
    }

    @Test
    void resolve_LastFloorOfSection_IsMiniboss() {
        ResolvedFloor resolved = layout().resolve(5);
        assertEquals(SectionId.CAVES, resolved.section().id());
        assertEquals(FloorRole.MINIBOSS, resolved.role());
    }

    @Test
    void resolve_FloorBetweenSections_IsRest() {
        ResolvedFloor resolved = layout().resolve(6);
        assertEquals(FloorRole.REST, resolved.role());
        assertEquals(SectionId.RUINS, resolved.section().id(), "Rest floor opens the next section");
    }

    @Test
    void resolve_SecondSectionStartsAfterRest() {
        ResolvedFloor resolved = layout().resolve(7);
        assertEquals(SectionId.RUINS, resolved.section().id());
        assertEquals(FloorRole.NORMAL, resolved.role());
    }

    @Test
    void resolve_SecondSectionMiniboss() {
        assertEquals(FloorRole.MINIBOSS, layout().resolve(11).role());
    }

    @Test
    void resolve_UncoveredFloor_Throws() {
        assertThrows(IllegalArgumentException.class, () -> layout().resolve(12));
    }

    @Test
    void resolve_SeedIsWorldSeedPlusFloor() {
        assertEquals(SEED + 3, layout().resolve(3).seed());
    }

    @Test
    void resolve_IsDeterministic() {
        ResolvedFloor a = layout().resolve(4);
        ResolvedFloor b = layout().resolve(4);
        assertEquals(a.variation(), b.variation());
        assertEquals(a.role(), b.role());
        assertEquals(a.seed(), b.seed());
    }
}
