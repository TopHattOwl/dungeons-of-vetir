package com.tophattowl.dungeonsofvetir.game.dungeon.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Resolves an absolute floor number to its section, variation and role.
 * <p>
 * Floor spans are derived from the ordered descriptors: sections are laid out
 * back to back and a single REST floor is inserted between consecutive sections.
 * Seeds are deterministic per floor ({@code worldSeed + floorNumber}).
 */
public class WorldLayout {

    private final long worldSeed;
    private final List<WorldSection> sections = new ArrayList<>();
    private final Map<Integer, WorldSection> restFloors = new HashMap<>();

    public WorldLayout(long worldSeed, List<SectionDescriptor> descriptors) {
        this.worldSeed = worldSeed;
        build(descriptors);
    }

    private void build(List<SectionDescriptor> descriptors) {
        int floorCursor = 1;

        for (int i = 0; i < descriptors.size(); i++) {
            SectionDescriptor descriptor = descriptors.get(i);
            int start = floorCursor;
            int end = start + descriptor.length() - 1;
            boolean hasRestAfter = i < descriptors.size() - 1;
            int restFloor = hasRestAfter ? end + 1 : -1;

            sections.add(new WorldSection(
                descriptor.id(), descriptor.name(),
                start, end, end, restFloor, pickVariation(descriptor, start)
            ));

            floorCursor = hasRestAfter ? end + 2 : end + 1;
        }

        // Rest floors inherit the flavor of the section they open.
        for (int i = 0; i < sections.size() - 1; i++) {
            WorldSection section = sections.get(i);
            if (section.hasRestFloor()) {
                restFloors.put(section.restFloor(), sections.get(i + 1));
            }
        }
    }

    /**
     * Picks the single variation used for the whole section, deterministically
     * from the world seed so a run always produces the same section flavor.
     */
    private SectionVariation pickVariation(SectionDescriptor descriptor, int startFloor) {
        List<SectionVariation> candidates = descriptor.variations();
        if (candidates.size() == 1) return candidates.get(0);

        Random rng = new Random(worldSeed + (long) startFloor * 31L);
        return candidates.get(rng.nextInt(candidates.size()));
    }

    public ResolvedFloor resolve(int floorNumber) {
        long seed = worldSeed + floorNumber;

        WorldSection restSection = restFloors.get(floorNumber);
        if (restSection != null) {
            return new ResolvedFloor(restSection, restSection.variation(), FloorRole.REST, floorNumber, seed);
        }

        for (WorldSection section : sections) {
            if (section.contains(floorNumber)) {
                FloorRole role = floorNumber == section.minibossFloor()
                    ? FloorRole.MINIBOSS
                    : FloorRole.NORMAL;
                return new ResolvedFloor(section, section.variation(), role, floorNumber, seed);
            }
        }

        throw new IllegalArgumentException("No section covers floor " + floorNumber);
    }

    public List<WorldSection> sections() {
        return List.copyOf(sections);
    }
}
