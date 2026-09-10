package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

public class DescendAction extends Action {

    public DescendAction(Entity owner) {
        super(ActionType.DESCEND_STAIRS, owner);
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        PositionComponent pos = owner.getComponent(PositionComponent.class);
        if (gameWorld.getCurrentLevel().getTile(pos.getX(), pos.getY()).type == TileType.STAIRS_DOWN) {
            possible();
        }
        return this;
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        if (notPossible()) return this;
        gameWorld.enterLevel(gameWorld.getCurrentFloor() + 1);
        success();
        return this;
    }

    @Override
    public String toString() {
        return "[DescendAction]: owner=" + owner;
    }
}
