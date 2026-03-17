package com.tophattowl.dungeonsofvetir.game.turn_system;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.PassAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.AiComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityAddedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.TurnPassedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TimeTurnManager {
    private final List<EventBus.ListenerHandle<?>> listenerHandles = new ArrayList<>();


    private PriorityQueue<Entity> actorQueue;
    private TurnEvent turnEvent;
    public TimeTurnManager() {

        // entity with the lowest time value is at front
        actorQueue = new PriorityQueue<>(
            (a, b) -> {
                int timeA = a.getComponent(TimeValueComponent.class).timeValueSum;
                int timeB = b.getComponent(TimeValueComponent.class).timeValueSum;

                // primary sort
                if (timeA != timeB) {
                    return Integer.compare(timeA, timeB);
                }

                // tiebreaker for turn event
                boolean aIsTurnEvent = a == turnEvent;
                boolean bIsTurnEvent = b == turnEvent;
                if (aIsTurnEvent) return -1;
                if (bIsTurnEvent) return 1;

                // tiebreaker for player (player goes first)
                boolean aIsPlayer = a.hasComponent(PlayerComponent.class);
                boolean bIsPlayer = b.hasComponent(PlayerComponent.class);
                if (aIsPlayer) return -1;
                if (bIsPlayer) return 1;

                return 0;
            }
        );

        turnEvent = new TurnEvent(100);
        actorQueue.add(turnEvent);

        listenerHandles.add(EventBus.on(EntityAddedEvent.class, e -> {
            addActor(e.entity());
        }));
        listenerHandles.add(EventBus.on(EntityRemovedEvent.class, e -> {
            removeActor(e.entity());
        }));
    }


    public void processNext(GameWorld gameWorld) {
        Entity currentEntity = actorQueue.poll();
        if (currentEntity == null) {
            DebugLogger.log(DebugLogger.Category.TURN, DebugLogger.Level.WARNING, "TimeTurnManager",
                "Actor queue has no actors :("
            );
            return;
        }

        TimeValueComponent timeValueComp = currentEntity.getComponent(TimeValueComponent.class);

        // turn event is next -> pass turn
        if (currentEntity == turnEvent) {
            passTurn();
            return;
        }

        // if player next up -> wait for input
        if (currentEntity == gameWorld.getPlayer()) {
            PlayerComponent playerComp = gameWorld.getPlayer().getComponent(PlayerComponent.class);
            playerComp.isPlayersTurn = true;
            return;
        }

        // other actors
        processActor(currentEntity, gameWorld);
        addActor(currentEntity);
    }

    public void onPlayerActionCompleted(GameWorld gameWorld) {
        // add player back to queue
        addActor(gameWorld.getPlayer());
    }


    private void processActor(Entity entity, GameWorld gameWorld) {
        TimeValueComponent timeComp = entity.getComponent(TimeValueComponent.class);

        Action action;
        AiComponent aiComp = entity.getComponent(AiComponent.class);

        if (aiComp != null && aiComp.aiStrategy != null) {
            action = aiComp.aiStrategy.chooseAction(entity, gameWorld);
        } else {
            action = new PassAction(entity);
        }

        Action resultAction = action.execute(gameWorld);

        if(resultAction.isSuccess()) timeComp.addTime(resultAction.getCost());
    }

    private void addActor(Entity entity) {
        actorQueue.add(entity);
    }

    private void removeActor(Entity entity) {
        actorQueue.remove(entity);
    }

    public Entity peekNext() {
        return actorQueue.peek();
    }

    private void normalize() {

    }

    private void passTurn() {
        turnEvent.passTurn();

        // TODO: process projectiles here

        // add turn event back after calling its pass turn method
        addActor(turnEvent);
        EventBus.emit(new TurnPassedEvent());
    }

    public void dispose() {
        listenerHandles.forEach(EventBus::off);
        actorQueue.clear();
    }

}
