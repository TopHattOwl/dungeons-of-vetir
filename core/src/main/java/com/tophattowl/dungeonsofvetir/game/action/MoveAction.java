package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.Direction;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.systems.MovementSystem;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class MoveAction extends Action {

    private Direction direction;

    public MoveAction(Direction dir, Entity owner) {
        super(ActionType.MOVE, owner);
        this.direction = dir;
    }

    @Override
    public boolean execute(GameWorld gameWorld) {
        MovementSystem moveSystem = gameWorld.getSystem(MovementSystem.class);
        boolean isSuccess = moveSystem.tryMove(this, gameWorld);

        if (isSuccess) {
            // add energy cost to user
        }
        return isSuccess;
    }

    @Override
    public String toString() {
        return "[MoveAction]: " + direction.toString() + ", cost: " + cost + ", owner: " + owner.toString();
    }

    public Direction getDirection() {
        return direction;
    }
}
