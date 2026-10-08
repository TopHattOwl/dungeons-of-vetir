package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.ActionCompletedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

/**
 * Stateless helper that runs an {@link Action} through its two phases and charges the owner time <br>
 * The world is passed explicitly (no global state)
 */
public final class ActionHandler {

    private ActionHandler() {}

    public static Action prepareAction(Entity entity, Action action, GameWorld gameWorld) {
        return action.prepare(gameWorld);
    }

    public static Action executeAction(Entity entity, Action action, GameWorld gameWorld) {
        if (action.notPossible()) {
            return action;
        }

        TimeValueComponent timeComp = entity.getComponent(TimeValueComponent.class);
        Action executedAction = action.execute(gameWorld);

        // actions must never return null, treat a null result as a failed action
        if (executedAction == null) {
            DebugLogger.log(DebugLogger.Category.ACTION, DebugLogger.Level.WARNING, "ActionHandler",
                "Action returned null on execute: " + action
            );
            return action;
        }

        if (executedAction.isSuccess()) {
            timeComp.addTime(executedAction.getCost());
            EventBus.emit(new ActionCompletedEvent(entity, executedAction));
        }
        return executedAction;
    }
}
