package com.tophattowl.dungeonsofvetir.game.event.events.input;

import com.tophattowl.dungeonsofvetir.game.event.events.Event;

public record UiKeyTypedEvent(char keyChar) implements Event {}
