package com.tophattowl.dungeonsofvetir.game.actors.components;


import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.input.InputMode;

public class PlayerComponent implements Component {
    public boolean isPlayersTurn;
    private InputMode inputMode;
    private InputMode savedInputMode;


    public PlayerComponent() {
        this(InputMode.PLAYING);
    }

    public PlayerComponent(InputMode inputMode) {
        isPlayersTurn = false;
        this.inputMode = inputMode;
    }


    public InputMode getInputMode() {
        return inputMode;
    }

    public void saveInputMode() {
        savedInputMode = inputMode;
    }

    public void setInputMode(InputMode inputMode) {
        this.inputMode = inputMode;
    }

    public void restoreInputMode() {
        inputMode = savedInputMode;
    }
}
