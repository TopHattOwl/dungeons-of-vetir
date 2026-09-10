package com.tophattowl.dungeonsofvetir.game.dungeon.section;

/**
 * A section resolved to concrete floor numbers.
 * <p>
 * {@code variation} is chosen once for the whole section, so every floor in the
 * span shares the same flavor. {@code restFloor} is the transition floor that opens
 * the next section, or -1 when this is the final section.
 */
public record WorldSection(
    SectionId id,
    String name,
    int startFloor,
    int endFloor,
    int minibossFloor,
    int restFloor,
    SectionVariation variation
) {
    public boolean contains(int floorNumber) {
        return floorNumber >= startFloor && floorNumber <= endFloor;
    }

    public boolean hasRestFloor() {
        return restFloor > 0;
    }
}
