package com.tophattowl.dungeonsofvetir.game.event.events;

import com.tophattowl.dungeonsofvetir.game.world.Place;

/**
 * Emitted after the world transitions to a new place. Display layers use it to
 * retheme the tileset and recenter the camera.
 */
public record LevelChangedEvent(Place place) implements Event {
}
