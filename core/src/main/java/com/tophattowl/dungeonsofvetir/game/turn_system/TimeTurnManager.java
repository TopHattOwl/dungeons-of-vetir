package com.tophattowl.dungeonsofvetir.game.turn_system;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.ECS.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityAddedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.TestEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.TurnPassedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.Arrays;
import java.util.PriorityQueue;

public class TimeTurnManager {

    PriorityQueue<Entity> actorQueue;
    TurnEvent turnEvent;

    public TimeTurnManager() {

        // entity with the lowest time value is at front
        actorQueue = new PriorityQueue<>(
            (a, b) -> {
                int timeCompA = a.getComponent(TimeValueComponent.class).timeValueSum;
                int timeCompB = b.getComponent(TimeValueComponent.class).timeValueSum;

                return Integer.compare(timeCompA,  timeCompB);
            }
        );

        turnEvent = new TurnEvent(100);
        actorQueue.add(turnEvent);

        EventBus.on(EntityAddedEvent.class, e -> {
            addActor(e.entity());
            System.out.println("added actor");
        });
        EventBus.on(EntityRemovedEvent.class, e -> {
            removeActor(e.entity());
            System.out.println("removed actor");
        });
    }



    public void processNext(GameWorld gameWorld) {
        System.out.println(Arrays.toString(actorQueue.toArray()));

        Entity currentEntity = actorQueue.poll();
        if (currentEntity == null) throw new RuntimeException("TimeTurnManager has no actors in the queue");

        TimeValueComponent timeValueComp = currentEntity.getComponent(TimeValueComponent.class);

        // turn event is next -> pass turn
        if (currentEntity == turnEvent) {
            System.out.println("[TimeTurnManager] Turn event up next, passing turn]");
            passTurn();
            return;
        }

        // if player next up -> wait for input
        if (currentEntity == gameWorld.getPlayer()) {
            System.out.println("[TimeTurnManager] Player up next, waiting for input");
            PlayerComponent playerComp = gameWorld.getPlayer().getComponent(PlayerComponent.class);
            playerComp.isPlayersTurn = true;
            return;
        }

        // other actors
        System.out.println("[TimeTurnManager] processing action for actor: " + currentEntity);
        timeValueComp.addTime(100);   // PLACEHOLDER for enemy action
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

    private void passTurn() {
        turnEvent.passTurn();

        // TODO: process projectiles here

        // add turn event back after calling its pass turn method
        addActor(turnEvent);
        EventBus.emit(new TurnPassedEvent());
    }

}
