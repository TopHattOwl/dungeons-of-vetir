package com.tophattowl.dungeonsofvetir.game.ECS.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.action.PassAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.AttackAction;
import com.tophattowl.dungeonsofvetir.game.action.MoveAction;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.ActionCompletedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityMovedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public class MovementSystem implements GameSystem {

    public Action tryMove(MoveAction moveAction, GameWorld gameWorld) {
        Entity owner = moveAction.getOwner();
        PositionComponent posComp = owner.getComponent(PositionComponent.class);
        int newX = posComp.getX() + moveAction.getDirection().getDx();
        int newY = posComp.getY() + moveAction.getDirection().getDy();

        if (!gameWorld.getCurrentLevel().isWalkable(newX, newY)) return moveAction;

        // if owner is at pos, attack
        Entity entityAtPos = gameWorld.getEntityAt(newX, newY);
        if (entityAtPos != null) {
            IdentityComponent ownerIdComp = owner.getComponent(IdentityComponent.class);
            IdentityComponent entityIdComp = entityAtPos.getComponent(IdentityComponent.class);

            FactionRelation.Relation relation = FactionRelation.getRelation(ownerIdComp.faction, entityIdComp.faction);

            switch (relation) {
                case FRIENDLY, NEUTRAL -> {
                    return gameWorld.actionHandler.processAction(owner, new PassAction(owner));
                }
                case HOSTILE -> {
                    return gameWorld.actionHandler.processAction(owner, new AttackAction(owner, entityAtPos));
                }
            }
        }

        Point newPos = new Point(newX, newY);
        gameWorld.moveEntity(owner, newPos);
        posComp.set(newPos);

        moveAction.success();
        EventBus.emit(new EntityMovedEvent(owner, newPos));
        EventBus.emit(new ActionCompletedEvent(owner, moveAction));

        return moveAction;
    }
}
