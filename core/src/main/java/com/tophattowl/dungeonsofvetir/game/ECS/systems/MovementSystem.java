package com.tophattowl.dungeonsofvetir.game.ECS.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.action.*;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityMovedEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;
import com.tophattowl.dungeonsofvetir.util.Direction;

public class MovementSystem implements GameSystem {

    public static Action prepareMove(MoveAction moveAction, GameWorld gameWorld) {
        Entity owner = moveAction.getOwner();
        if (moveAction.getDirection() == Direction.STAY) {
            return ActionHandler.prepareAction(owner, new PassAction(owner));
        }

        PositionComponent posComp = owner.getComponent(PositionComponent.class);
        int newX = posComp.getX() + moveAction.getDirection().getDx();
        int newY = posComp.getY() + moveAction.getDirection().getDy();
        moveAction.setNewPos(new Point(newX, newY));

        if (!gameWorld.getCurrentLevel().isWalkable(newX, newY)) return moveAction;

        // if owner is at pos, attack
        Entity entityAtPos = gameWorld.getEntityAt(newX, newY);
        if (entityAtPos != null) {
            IdentityComponent ownerIdComp = owner.getComponent(IdentityComponent.class);
            IdentityComponent entityIdComp = entityAtPos.getComponent(IdentityComponent.class);

            FactionRelation.Relation relation = FactionRelation.getRelation(ownerIdComp.faction, entityIdComp.faction);

            switch (relation) {
                // TODO: talk, or push action
                case FRIENDLY, NEUTRAL -> {
                    return ActionHandler.prepareAction(owner, new PassAction(owner));
                }
                case HOSTILE -> {
                    return ActionHandler.prepareAction(owner, new MeleeAttackAction(owner, entityAtPos));
                }
            }
        }
        moveAction.possible();
        return moveAction;
    }

    public static Action executeMove(MoveAction moveAction, GameWorld gameWorld) {
        Entity owner = moveAction.getOwner();
        PositionComponent posComp = owner.getComponent(PositionComponent.class);

        Point newPos = moveAction.getNewPos();
        gameWorld.moveEntity(owner, newPos);
        posComp.set(newPos);

        moveAction.success();
        EventBus.emit(new EntityMovedEvent(owner, newPos));
        return moveAction;
    }
}
