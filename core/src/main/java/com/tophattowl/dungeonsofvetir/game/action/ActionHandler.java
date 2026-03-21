package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.ActionCompletedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class ActionHandler {
    private final GameWorld gameWorld;

    public ActionHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public Action prepareAction(Entity entity, Action action) {
        return action.prepare(gameWorld);
    }

    public Action executeAction(Entity entity, Action action) {
        if (action.notPossible()) {
            return action;
        }

        TimeValueComponent timeComp = entity.getComponent(TimeValueComponent.class);
        Action executedAction = action.execute(gameWorld);

        if (executedAction.isSuccess()) {
            timeComp.addTime(executedAction.getCost());
            EventBus.emit(new ActionCompletedEvent(entity, executedAction));
        }

        return executedAction;
    }

    private void addPlayerActionToHistory() {
        // TODO: store player's last 10 action somewhere
    }
}
