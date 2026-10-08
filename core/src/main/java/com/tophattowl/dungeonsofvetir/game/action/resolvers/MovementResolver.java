package com.tophattowl.dungeonsofvetir.game.action.resolvers;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.ActionHandler;
import com.tophattowl.dungeonsofvetir.game.action.MoveAction;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.actors.faction.FactionRelation;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityMovedEvent;
import com.tophattowl.dungeonsofvetir.game.factory.action.ActionFactory;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;
import com.tophattowl.dungeonsofvetir.util.Direction;

/**
 * Resolves a {@link MoveAction} on demand: decide if the move is possible (or
 * becomes a pass/attack) and carry it out
 */
public final class MovementResolver {

    private MovementResolver() {}

    public static Action prepareMove(MoveAction moveAction, GameWorld gameWorld) {
        Entity owner = moveAction.getOwner();
        if (moveAction.getDirection() == Direction.STAY) {
            return ActionHandler.prepareAction(owner, ActionFactory.createPassAction(owner), gameWorld);
        }

        PositionComponent posComp = owner.getComponent(PositionComponent.class);
        int newX = posComp.getX() + moveAction.getDirection().getDx();
        int newY = posComp.getY() + moveAction.getDirection().getDy();
        moveAction.setNewPos(new Point(newX, newY));

        if (!gameWorld.getCurrentLevel().isWalkable(newX, newY)) return moveAction;

        // if another entity is there, the move becomes a pass or an attack
        Entity entityAtPos = gameWorld.getEntity(newX, newY);
        if (entityAtPos != null) {
            IdentityComponent ownerIdComp = owner.getComponent(IdentityComponent.class);
            IdentityComponent entityIdComp = entityAtPos.getComponent(IdentityComponent.class);

            FactionRelation.Relation relation = gameWorld.getFactionRelations()
                .getRelation(ownerIdComp.faction, entityIdComp.faction);

            return switch (relation) {
                // TODO: talk, or push action
                case FRIENDLY, NEUTRAL ->
                    ActionHandler.prepareAction(owner, ActionFactory.createPassAction(owner), gameWorld);
                case HOSTILE -> {
                    if (!gameWorld.constraints().can(owner, Capability.ATTACK)) {
                        // combat not allowed here (e.g. overworld/settlement): blocked
                        yield ActionHandler.prepareAction(owner, ActionFactory.createPassAction(owner), gameWorld);
                    }
                    yield ActionHandler.prepareAction(owner, ActionFactory.createMeleeAttackAction(owner, entityAtPos), gameWorld);
                }
            };
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
