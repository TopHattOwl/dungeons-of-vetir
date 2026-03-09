package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class PassAction extends Action {
    public PassAction(Entity owner) {
        super(ActionType.PASS, owner);
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return this;
    }
}
