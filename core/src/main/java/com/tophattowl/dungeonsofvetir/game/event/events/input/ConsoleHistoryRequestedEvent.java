package com.tophattowl.dungeonsofvetir.game.event.events.input;

import com.tophattowl.dungeonsofvetir.game.event.events.Event;

/**
 * Requests the debug console to cycle command history
 * {@code direction} is -1 for previous (up) and +1 for next (down)
 */
public record ConsoleHistoryRequestedEvent(int direction) implements Event {}
