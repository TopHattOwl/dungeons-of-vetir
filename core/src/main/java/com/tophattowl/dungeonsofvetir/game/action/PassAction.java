package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class PassAction extends Action {
    public PassAction(Entity owner) {
        super(ActionType.PASS, owner);
    }

    @Override
    public boolean execute(GameWorld gameWorld) {
        return true;
    }
}
