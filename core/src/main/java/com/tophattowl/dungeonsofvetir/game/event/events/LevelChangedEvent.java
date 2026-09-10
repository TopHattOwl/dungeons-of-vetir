package com.tophattowl.dungeonsofvetir.game.event.events;

import com.tophattowl.dungeonsofvetir.game.dungeon.section.ResolvedFloor;

/**
 * Emitted after the world transitions to a new floor
 * Display layers use it to retheme the tileset and recenter the camera
 */
public record LevelChangedEvent(int floorNumber, ResolvedFloor resolved) implements Event {
}
