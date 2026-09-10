package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

public class AscendAction extends Action {

    public AscendAction(Entity owner) {
        super(ActionType.ASCEND_STAIRS, owner);
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        if (gameWorld.getCurrentFloor() <= 1) return this;

        PositionComponent pos = owner.getComponent(PositionComponent.class);
        if (gameWorld.getCurrentLevel().getTile(pos.getX(), pos.getY()).type == TileType.STAIRS_UP) {
            possible();
        }
        return this;
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        if (notPossible()) return this;
        gameWorld.enterLevel(gameWorld.getCurrentFloor() - 1);
        success();
        return this;
    }

    @Override
    public String toString() {
        return "[AscendAction]: owner=" + owner;
    }
}
