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
    public Action execute(GameWorld gameWorld) {
        MovementSystem moveSystem = gameWorld.getSystem(MovementSystem.class);

        return moveSystem.tryMove(this, gameWorld);
    }

    @Override
    public String toString() {
        return "[MoveAction]: " + direction.toString() + ", cost: " + cost + ", owner: " + owner.toString();
    }

    public Direction getDirection() {
        return direction;
    }
}
