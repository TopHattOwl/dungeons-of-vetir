package com.tophattowl.dungeonsofvetir.util;

import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.UiKeyTypedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class DebugConsole {
    private boolean active = false;
    private final StringBuilder inputBuffer = new StringBuilder();
    private final List<String> outputLines = new ArrayList<>();
    private final List<String> history =  new ArrayList<>();
    private int historyIndex = -1;

    private static final int MAX_OUTPUT_LINES = 50;

    private GameWorld gameWorld;

    private final List<EventBus.ListenerHandle<?>> listenerHandles = new ArrayList<>();

    public DebugConsole() {
        listenerHandles.add(EventBus.on(ConsoleRequestedEvent.class, e -> {
            toggle();
        }));
        listenerHandles.add(EventBus.on(UiKeyTypedEvent.class, e -> {
            handleChar(e.keyChar());
        }));

    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    // --------------------------------------------------
    // visibility
    public void toggle() { active = !active; }
    public boolean isActive() { return active; }

    // --------------------------------------------------
    // input -- called from InputHandler
    public void handleChar(char c) {
        if (!active) return;

        // backspace
        if (c == '\b') {
            if (!inputBuffer.isEmpty()) {
                inputBuffer.deleteCharAt(inputBuffer.length() - 1);
            }
            return;
        }

        if (c == '\r' || c == '\n') {
            submit();
        } else  {
            inputBuffer.append(c);
        }
    }



    // --------------------------------------------------
    // command execution
    private void submit() {
        // submit command

    }


    // --------------------------------------------------
    // getter
    public List<String> getOutputLines() {
        return outputLines;
    }

    public String getInputString() {
        return inputBuffer.toString();
    }

    public void dispose() {
        listenerHandles.forEach(EventBus::off);
        listenerHandles.clear();
    }
}
