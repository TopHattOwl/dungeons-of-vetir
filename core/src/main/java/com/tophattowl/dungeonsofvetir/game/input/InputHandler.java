package com.tophattowl.dungeonsofvetir.game.input;

import com.badlogic.gdx.InputProcessor;
import com.tophattowl.dungeonsofvetir.game.Direction;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.MoveAction;
import com.tophattowl.dungeonsofvetir.game.action.PassAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;

public class InputHandler implements InputProcessor {

    private Entity player;
    private Action pendingAction = null;

    public InputHandler(Entity player) {
        this.player = player;
    }

    public Action getPendingAction() {
        Action action = pendingAction;
        pendingAction = null;

        return action;
    }

    @Override
    public boolean keyDown(int keyCode) {
        Direction dir = Direction.fromKeyCode(keyCode);

        if (dir != null) {
            if (dir == Direction.STAY) {
                pendingAction = new PassAction(player);
            } else {
                pendingAction = new MoveAction(dir, player);
            }
            return true;
        }

        // handle other keys later


        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
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
}
