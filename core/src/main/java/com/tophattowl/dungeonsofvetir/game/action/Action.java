package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public abstract class Action {
    protected int cost;
    protected ActionType actionType;
    protected Entity owner;

    public Action(ActionType actionType, Entity owner) {
        this.actionType = actionType;
        this.owner = owner;
        this.cost = actionType.getBaseCost();
    }

    public abstract boolean execute(GameWorld gameWorld);

    public int getCost() {
        return cost;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Entity getOwner() {
        return owner;
    }

    public String toString() {
        return "[Action] " + actionType.toString() + ", cost: " + cost + ", owner: " + owner.toString();
    }
}
