package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.world.Point;
import com.tophattowl.dungeonsofvetir.util.Direction;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.resolvers.MovementResolver;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public class MoveAction extends Action {

    private final Direction direction;
    private Point newPos;

    public MoveAction(Direction dir, Entity owner) {
        super(ActionType.MOVE, owner);
        this.direction = dir;
    }

    public void setNewPos(Point newPos) {
        this.newPos = newPos;
    }

    public Point getNewPos() {
        return newPos;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public Action prepare(GameWorld gameWorld) {
        if (!gameWorld.constraints().can(owner, Capability.MOVE)) return this;
        return MovementResolver.prepareMove(this, gameWorld);
    }

    @Override
    public Action execute(GameWorld gameWorld) {
        return MovementResolver.executeMove(this, gameWorld);
    }

    @Override
    public String toString() {
        return "[MoveAction]: " + direction.toString() + ", cost: " + cost + ", owner: " + owner.toString();
    }

}
