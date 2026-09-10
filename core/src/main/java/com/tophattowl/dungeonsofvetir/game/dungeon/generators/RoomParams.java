package com.tophattowl.dungeonsofvetir.game.dungeon.generators;

/**
 * Tunables for the room-and-corridor generator
 */
public record RoomParams(
    int roomCount,
    int minRoomSize,
    int maxRoomSize,
    int maxPlacementAttempts,
    int floorVariants
) {
    public static final RoomParams DEFAULT = new RoomParams(10, 5, 11, 200, 4);
}
