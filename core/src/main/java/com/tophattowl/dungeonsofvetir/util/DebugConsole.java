package com.tophattowl.dungeonsofvetir.util;

import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;

public class DebugConsole {
    private boolean visible = false;
    private final StringBuilder inputBuffer = new StringBuilder();
    private final List<String> outputLines = new ArrayList<>();
    private final List<String> history =  new ArrayList<>();
    private int historyIndex = -1;

    private static final int MAX_OUTPUT_LINES = 50;

    private GameWorld gameWorld;
    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    // visibility
    public void toggle() {visible = !visible; }
    public boolean isVisible() {
        return visible;
    }

    // input -- called from InputHandler
    public void handleChar(char c) {
        if (!visible) return;

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


    // command execution
    private void submit() {
        // submint command
    }


    // commands
}
