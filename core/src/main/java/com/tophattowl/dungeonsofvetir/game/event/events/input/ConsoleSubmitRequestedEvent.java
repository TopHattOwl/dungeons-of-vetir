package com.tophattowl.dungeonsofvetir.game.event.events.input;

import com.tophattowl.dungeonsofvetir.game.event.events.Event;

/**
 * Requests the debug console to submit its current input line
 */
public record ConsoleSubmitRequestedEvent() implements Event {}
