package com.tophattowl.dungeonsofvetir.game.turn_system;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.ActionHandler;
import com.tophattowl.dungeonsofvetir.game.action.MoveAction;
import com.tophattowl.dungeonsofvetir.game.action.PassAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityAddedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.TurnPassedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.util.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class TimeTurnManager {
    private final List<EventBus.ListenerHandle<?>> listenerHandles = new ArrayList<>();

    private PriorityQueue<Entity> actorQueue;
    private TurnEvent turnEvent;

    public TimeTurnManager() {
        initActorQueue();
        addListeners();
    }

    public int getTurnEventTime() {

        return turnEvent.getComponent(TimeValueComponent.class).timeValueSum;
    }

    public void processNext(GameWorld gameWorld) {
        DebugLogger.log(DebugLogger.Category.TURN, "TimeTurnManager", toString());


        DebugLogger.log(DebugLogger.Category.TURN, "TimeTurnManager",
            "Processing next actor: \n" + peekNext()
        );

        Entity currentEntity = actorQueue.poll();
        if (currentEntity == null) {
            DebugLogger.log(DebugLogger.Category.TURN, DebugLogger.Level.WARNING, "TimeTurnManager",
                "Actor queue has no actors :("
            );
            return;
        }

        // turn event is next -> pass turn
        if (currentEntity == turnEvent) {
            DebugLogger.log(DebugLogger.Category.TURN, "TimeTurnManager",
                "Turn event next, passing turn."
            );
            passTurn();
            return;
        }

        // if player next up -> wait for input
        if (currentEntity == gameWorld.getPlayer()) {
            DebugLogger.log(DebugLogger.Category.TURN, "TimeTurnManager",
                "player next, exiting method"
            );
            PlayerComponent playerComp = gameWorld.getPlayer().getComponent(PlayerComponent.class);
            playerComp.isPlayersTurn = true;
            return;
        }

        // other actors
        DebugLogger.log(DebugLogger.Category.TURN, "TimeTurnManager",
            "Other entity next, processing them."
        );
        processActor(currentEntity, gameWorld);
        addActor(currentEntity);
    }

    public void onPlayerActionCompleted(GameWorld gameWorld) {
        // add player back to queue
        addActor(gameWorld.getPlayer());
    }


    private void processActor(Entity entity, GameWorld gameWorld) {
        Action action = chooseActionForAi(entity , gameWorld);

        Action preparedAction = ActionHandler.prepareAction(entity, action);
        if (preparedAction.notPossible()) {
            ActionHandler.executeAction(entity, new PassAction(entity));
        }

        Action executedAction = ActionHandler.executeAction(entity, preparedAction);
        if (!executedAction.isSuccess()) {
            ActionHandler.executeAction(entity, new PassAction(entity));
        }
    }

    private Action chooseActionForAi(Entity entity , GameWorld gameWorld) {
        AiComponent aiComp = entity.getComponent(AiComponent.class);
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        Faction faction = entity.getComponent(IdentityComponent.class).faction;

        Direction dir = gameWorld.dijkstraMapManager.getBestMove(
            posComp.getX(), posComp.getY(),
            aiComp.weightMap,  faction
        );

        return new MoveAction(dir, entity);
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

    private void initActorQueue() {
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

                // fallback tiebreaker


                return 0;
            }
        );

        turnEvent = new TurnEvent(100);
        actorQueue.add(turnEvent);
    }

    private void addListeners() {
        listenerHandles.add(EventBus.on(EntityAddedEvent.class, e -> {
            addActor(e.entity());
        }));
        listenerHandles.add(EventBus.on(EntityRemovedEvent.class, e -> {
            removeActor(e.entity());
        }));
    }

    private void passTurn() {
        turnEvent.passTurn();

        // TODO: process projectiles here
        // add turn event back after calling its pass turn method
        addActor(turnEvent);

        EventBus.emit(new TurnPassedEvent());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Entity> sorted = new ArrayList<>(actorQueue);
        sorted.sort(actorQueue.comparator());

        for (Entity entity : sorted) {
            int time =  entity.getComponent(TimeValueComponent.class).timeValueSum;
            sb.append(entity).append(" | ").append(time).append("\n");
        }

        return "TimeTurnManager, actor queue:\n" + sb;
    }

    public void dispose() {
        listenerHandles.forEach(EventBus::off);
        actorQueue.clear();
    }

}
