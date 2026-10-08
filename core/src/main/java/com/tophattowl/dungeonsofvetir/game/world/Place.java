package com.tophattowl.dungeonsofvetir.game.world;

/**
 * A location in the world: its stable {@link PlaceId} plus everything needed to
 * generate and theme the zone. Holds no live state; the generator is resolved from
 * the place's definition and kind (see {@code ZoneResolver}).
 */
public record Place(
    PlaceId id,
    int width,
    int height,
    long seed,
    TileTheme theme,
    PlaceRole role
) {
    public PlaceKind kind() {
        return id.kind();
    }

    public int depth() {
        return id.depth();
    }

    public boolean isDungeonFloor() {
        return id.isDungeonFloor();
    }
}
