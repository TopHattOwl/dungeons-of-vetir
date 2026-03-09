package com.tophattowl.dungeonsofvetir.game.ECS.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.MoveAction;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public class MovementSystem implements GameSystem{

    public Action tryMove(MoveAction moveAction, GameWorld gameWorld) {

        System.out.println("trying move with action:");
        System.out.println(moveAction);

        Entity entity = moveAction.getOwner();
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        int newX = posComp.getX() + moveAction.getDirection().getDx();
        int newY = posComp.getY() + moveAction.getDirection().getDy();

        if (!gameWorld.getCurrentLevel().isWalkable(newX, newY)) return moveAction;

        if (gameWorld.getEntityAt(newX, newY) != null) return moveAction;

        Point newPos = new Point(newX, newY);
        gameWorld.moveEntity(entity, newPos);
        System.out.println("new pos: " + newPos);
        posComp.set(newPos);

        moveAction.sucess();

        return moveAction;
    }
}
