package com.tophattowl.dungeonsofvetir.game.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

public abstract class Action {
    protected int cost;
    protected ActionType actionType;
    protected Entity owner;
    protected boolean isSuccess;
    protected boolean possible;

    public Action(ActionType actionType, Entity owner) {
        this.actionType = actionType;
        this.owner = owner;
        this.cost = actionType.getBaseCost();
        this.isSuccess = false;
        this.possible = false;
    }

    public abstract Action prepare(GameWorld gameWorld);

    public abstract Action execute(GameWorld gameWorld);

    public int getCost() {
        return cost;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public Entity getOwner() {
        return owner;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isPossible() {
        return possible;
    }

    public void possible() {
        possible = true;
    }

    public void success() {
        isSuccess = true;
    }

    public String toString() {
        return "[Action] " + actionType.toString() + ", cost: " + cost + ", owner: " + owner.toString();
    }
}
