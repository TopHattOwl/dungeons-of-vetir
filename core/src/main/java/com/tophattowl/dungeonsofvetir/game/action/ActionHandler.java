package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class ActionHandler {
    private GameWorld gameWorld;

    public ActionHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public Action processAction(Entity entity, Action action) {
        TimeValueComponent energyComp = entity.getComponent(TimeValueComponent.class);

        Action actionFinal = action.execute(gameWorld);

        if (actionFinal.isSuccess()) {
            energyComp.addTime(actionFinal.getCost());
            if (entity == gameWorld.getPlayer()) addPlayerActionToHistory();
        }

        return actionFinal;
    }

    private void addPlayerActionToHistory() {
        // TODO: store player's last 10 action somewhere
    }
}
