package com.tophattowl.dungeonsofvetir.game.ECS.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.GameSystem;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.AttackAction;
import com.tophattowl.dungeonsofvetir.game.action.MoveAction;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public class MovementSystem implements GameSystem {

    public Action tryMove(MoveAction moveAction, GameWorld gameWorld) {
        Entity entity = moveAction.getOwner();
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        int newX = posComp.getX() + moveAction.getDirection().getDx();
        int newY = posComp.getY() + moveAction.getDirection().getDy();

        if (!gameWorld.getCurrentLevel().isWalkable(newX, newY)) return moveAction;

        // if entity is at pos, attack
        if (gameWorld.getEntityAt(newX, newY) != null) {
            return new AttackAction(entity, gameWorld.getEntityAt(newX, newY)).execute(gameWorld);
        }

        Point newPos = new Point(newX, newY);
        gameWorld.moveEntity(entity, newPos);
        posComp.set(newPos);

        moveAction.success();

        return moveAction;
    }
}
