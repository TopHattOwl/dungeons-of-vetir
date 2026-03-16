package com.tophattowl.dungeonsofvetir.game.event.events.input;

import com.tophattowl.dungeonsofvetir.game.input.InputMode;

public record InputModeChangedEvent(InputMode oldMode, InputMode newMode) {}
