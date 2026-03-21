package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class PassAction extends Action {
    public PassAction(Entity owner) {
        super(ActionType.PASS, owner);
        possible();
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        return this;
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        success();
        return this;
    }
}
