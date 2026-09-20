package com.tophattowl.dungeonsofvetir.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.tophattowl.dungeonsofvetir.game.factory.action.ActionFactory;
import com.tophattowl.dungeonsofvetir.util.Direction;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.EventSubscriptions;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleActiveChangedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleHistoryRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleSubmitRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.ConsoleToggleRequestedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.InputModeChangedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.input.InventoryToggleRequestedEvent;

import java.util.Stack;


/**
 * Processes the input made by player
 * 1. Makes pending action if player makes a move that is an Action
 * 2. handles other inputs (that don't make actions) with events
 * <p>
 * The CONSOLE input mode is derived from the debug console's active state, so the
 * console can also be closed by commands (e.g. {@code exit}).
 */
public class InputHandler implements InputProcessor {
    private final Stack<InputMode> modeStack = new Stack<>();
    private final EventSubscriptions eventSubs = new EventSubscriptions();
    private final Entity player;

    private Action pendingAction = null;

    public InputHandler(Entity player) {
        this.player = player;
        modeStack.push(InputMode.PLAYING);
        player.getComponent(PlayerComponent.class).setInputMode(InputMode.PLAYING);
        eventSubs.on(ConsoleActiveChangedEvent.class, this::onConsoleActiveChanged);
    }

    public Action getPendingAction() {
        Action action = pendingAction;
        pendingAction = null;

        return action;
    }

    private void onConsoleActiveChanged(ConsoleActiveChangedEvent event) {
        InputMode current = player.getComponent(PlayerComponent.class).getInputMode();
        if (event.active() && current != InputMode.CONSOLE) {
            pushMode(InputMode.CONSOLE);
        } else if (!event.active() && current == InputMode.CONSOLE) {
            popMode();
        }
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
            // backtick toggles the debug console (see ConsoleActiveChangedEvent)
            case Input.Keys.GRAVE -> {
                EventBus.emit(new ConsoleToggleRequestedEvent());
                return true;
            }

            case Input.Keys.PERIOD -> {
                pendingAction = ActionFactory.createDescendAction(player);
                return true;
            }

            case Input.Keys.COMMA -> {
                pendingAction = ActionFactory.createAscendAction(player);
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

    /**
     * Console-specific keys are handled here (the game's input processor runs before
     * the Scene2D stage), everything else falls through to the text field.
     */
    private boolean handleConsoleInput(int keyCode) {
        switch (keyCode) {
            case Input.Keys.ESCAPE, Input.Keys.GRAVE -> {
                EventBus.emit(new ConsoleToggleRequestedEvent());
                return true;
            }
            case Input.Keys.UP -> {
                EventBus.emit(new ConsoleHistoryRequestedEvent(-1));
                return true;
            }
            case Input.Keys.DOWN -> {
                EventBus.emit(new ConsoleHistoryRequestedEvent(1));
                return true;
            }
            case Input.Keys.ENTER, Input.Keys.NUMPAD_ENTER -> {
                EventBus.emit(new ConsoleSubmitRequestedEvent());
                return true;
            }
            default -> {
                return false;
            }
        }
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

        if (mode == InputMode.PLAYING) {
            // '>' descends, '<' ascends (shift-free fallbacks are PERIOD/COMMA in keyDown)
            if (c == '>') {
                pendingAction = ActionFactory.createDescendAction(player);
                return true;
            }
            if (c == '<') {
                pendingAction = ActionFactory.createAscendAction(player);
                return true;
            }
        }

        // Everything else (including console text input) is handled by the Scene2D stage.
        return false;
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

    public void dispose() {
        eventSubs.unsubscribeAll();
    }
}
