package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.ActionCompletedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class ActionHandler {
    private static final ActionHandler INSTANCE = new ActionHandler();
    private GameWorld gameWorld;

    private ActionHandler() {}

    public static void setGameWorld(GameWorld gameWorld) {
        INSTANCE.gameWorld = gameWorld;
    }

    public static Action prepareAction(Entity entity, Action action) {
        return action.prepare(INSTANCE.gameWorld);
    }

    public static Action executeAction(Entity entity, Action action) {
        if (action.notPossible()) {
            return action;
        }

        TimeValueComponent timeComp = entity.getComponent(TimeValueComponent.class);
        Action executedAction = action.execute(INSTANCE.gameWorld);

        if (executedAction.isSuccess()) {
            timeComp.addTime(executedAction.getCost());
            EventBus.emit(new ActionCompletedEvent(entity, executedAction));
        }
        return executedAction;
    }

    public static Action executeActionDebug(Entity entity, Action action) {
        return action.execute(INSTANCE.gameWorld);
    }

    private void addPlayerActionToHistory() {
        // TODO: store player's last 10 action somewhere
    }
}
