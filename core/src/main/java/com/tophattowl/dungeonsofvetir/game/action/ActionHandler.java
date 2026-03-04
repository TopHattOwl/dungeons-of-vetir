package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.EnergyComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class ActionHandler {
    private GameWorld gameWorld;

    public ActionHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public boolean processAction(Entity entity, Action action) {
        EnergyComponent energyComp = entity.getComponent(EnergyComponent.class);

        boolean isSuccess = action.execute(gameWorld);

        if (isSuccess) {
            energyComp.addEnergy(action.getCost());
        }

        return isSuccess;
    }
}
