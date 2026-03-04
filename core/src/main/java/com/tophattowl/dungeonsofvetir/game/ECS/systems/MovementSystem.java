package com.tophattowl.dungeonsofvetir.game.ECS.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.action.MoveAction;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public class MovementSystem implements GameSystem{

    public boolean tryMove(MoveAction moveAction, GameWorld gameWorld) {

        System.out.println("trying move with action:");
        System.out.println(moveAction);

        Entity entity = moveAction.getOwner();
        PositionComponent posComp = entity.getComponent(PositionComponent.class);
        int newX = posComp.getX() + moveAction.getDirection().getDx();
        int newY = posComp.getY() + moveAction.getDirection().getDy();


        if (!gameWorld.getCurrentLevel().isWalkable(newX, newY)) return false;

        Point newPos = new Point(newX, newY);

        System.out.println("new pos: " + newPos);

        posComp.set(newPos);




        return true;
    }
}
