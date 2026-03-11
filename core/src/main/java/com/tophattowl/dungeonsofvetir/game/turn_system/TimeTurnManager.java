package com.tophattowl.dungeonsofvetir.game.turn_system;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityAddedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.TurnPassedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.PriorityQueue;

public class TimeTurnManager {

    PriorityQueue<Entity> actorQueue;
    TurnEvent turnEvent;
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

        EventBus.on(EntityAddedEvent.class, e -> {
            addActor(e.entity());
        });
        EventBus.on(EntityRemovedEvent.class, e -> {
            removeActor(e.entity());
        });
    }


    public void processNext(GameWorld gameWorld) {
//        System.out.print("\n ACTORS IN TURN MANAGER:\n[ ");
//        for (Entity entity : actorQueue) {
//            System.out.print(entity + " | ");
//        }
//        System.out.print("]\n");

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
//            System.out.println("[TimeTurnManager] Turn event up next, passing turn]");
            passTurn();
            return;
        }

        // if player next up -> wait for input
        if (currentEntity == gameWorld.getPlayer()) {
//            System.out.println("[TimeTurnManager] Player up next, waiting for input");
            PlayerComponent playerComp = gameWorld.getPlayer().getComponent(PlayerComponent.class);
            playerComp.isPlayersTurn = true;
            return;
        }

        // other actors
//        System.out.println("[TimeTurnManager] processing action for actor: " + currentEntity);
        timeValueComp.addTime(100);   // PLACEHOLDER for enemy actionna
        addActor(currentEntity);
    }

    public void onPlayerActionCompleted(GameWorld gameWorld) {
        // add player back to queue
        addActor(gameWorld.getPlayer());
    }

    public void addActor(Entity entity) {
        actorQueue.add(entity);
    }

    public void removeActor(Entity entity) {
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

}
