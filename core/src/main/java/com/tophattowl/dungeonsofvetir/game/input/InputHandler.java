package com.tophattowl.dungeonsofvetir.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.tophattowl.dungeonsofvetir.game.factory.action.ActionFactory;
import com.tophattowl.dungeonsofvetir.util.Direction;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleToggleRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.InputModeChangedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.InventoryToggleRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.UiKeyTypedEvent;

import java.util.Stack;


/**
 * Processes the input made by player
 * 1. Makes pending action if player makes a move that is an Action
 * 2. handles other inputs (that don't make actions) with events
 */
public class InputHandler implements InputProcessor {
    private final Stack<InputMode> modeStack = new Stack<>();
    private final Entity player;

    private Action pendingAction = null;

    public InputHandler(Entity player) {
        this.player = player;
        modeStack.push(InputMode.PLAYING);
        player.getComponent(PlayerComponent.class).setInputMode(InputMode.PLAYING);
    }

    public Action getPendingAction() {
        Action action = pendingAction;
        pendingAction = null;

        return action;
    }

    @Override
    public boolean keyDown(int keyCode) {
        InputMode mode = player.getComponent(PlayerComponent.class).getInputMode();

        switch (mode) {
            case PLAYING -> {
                return handlePlayingInput(keyCode);
            }
            case MENU -> {
                return handleMenuInput(keyCode);
            }
            case CONSOLE -> {
                return handleConsoleInput(keyCode);
            }
            default -> {
                return false;
            }
        }
    }

    private boolean handlePlayingInput(int keyCode) {
        Direction dir = Direction.fromKeyCode(keyCode);

        if (dir != null) {
            if (dir == Direction.STAY) {
                pendingAction = ActionFactory.createPassAction(player);
            } else {
                pendingAction = ActionFactory.createMoveAction(player, dir);
            }
            return true;
        }

        switch (keyCode) {
            // backtick
            case Input.Keys.GRAVE -> {
                pushMode(InputMode.CONSOLE);
                EventBus.emit(new ConsoleToggleRequestedEvent());
                return true;
            }

            case Input.Keys.I -> {
                pushMode(InputMode.INVENTORY);
                EventBus.emit(new InventoryToggleRequestedEvent());
                return true;
            }

            default -> {
                return false;
            }
        }
    }

    private boolean handleMenuInput(int keyCode) {
        return false;
    }

    private boolean handleConsoleInput(int keyCode) {

        switch (keyCode) {
            case Input.Keys.ESCAPE -> {
                popMode();
                EventBus.emit(new ConsoleToggleRequestedEvent());
            }

        }
        return false;
    }

    private void pushMode(InputMode newMode) {
        InputMode oldMode = modeStack.peek();
        modeStack.push(newMode);
        player.getComponent(PlayerComponent.class).setInputMode(newMode);
        EventBus.emit(new InputModeChangedEvent(oldMode, newMode));
    }

    private void popMode() {
        if (modeStack.isEmpty() || modeStack.size() == 1) {
            return;
        }

        InputMode oldMode = modeStack.pop();
        InputMode newMode = modeStack.peek();
        player.getComponent(PlayerComponent.class).setInputMode(newMode);
        EventBus.emit(new InputModeChangedEvent(oldMode, newMode));

    }


    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        InputMode mode = player.getComponent(PlayerComponent.class).getInputMode();

        if (mode != InputMode.CONSOLE) {
            return false;
        }

        EventBus.emit(new UiKeyTypedEvent(c));
        return true;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }
}
