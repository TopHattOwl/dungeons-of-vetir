package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.world.Exit;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.TransitionKind;

/**
 * Uses a stairs-down exit on Entity's tile
 */
public class DescendAction extends Action {

    public DescendAction(Entity owner) {
        super(ActionType.DESCEND_STAIRS, owner);
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        if (findExit(gameWorld) != null) possible();
        return this;
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        if (notPossible()) return this;
        gameWorld.useExit(findExit(gameWorld));
        success();
        return this;
    }

    private Exit findExit(GameWorld gameWorld) {
        PositionComponent pos = owner.getComponent(PositionComponent.class);
        Exit exit = gameWorld.exitAt(pos.getPosition());
        if (exit != null && exit.kind() == TransitionKind.STAIRS_DOWN) return exit;
        return null;
    }

    @Override
    public String toString() {
        return "[DescendAction]: owner=" + owner;
    }
}
