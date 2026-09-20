package com.tophattowl.dungeonsofvetir.game.event.events.input;

import com.tophattowl.dungeonsofvetir.game.event.events.Event;

/**
 * Emitted whenever the debug console's active state changes
 * <p>
 * Consumers (view, input handler) derive visibility/focus/input-mode from this
 */
public record ConsoleActiveChangedEvent(boolean active) implements Event {}
