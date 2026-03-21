package com.tophattowl.dungeonsofvetir.game.action;

public enum ActionType {
    NONE(0),

    PASS(100),
    MOVE(100),

    ATTACK(100),

    PICKUP(50),
    DROP(50),
    INTERACT(100),
    USE_ITEM(80);

    private final int baseCost;

    ActionType(int baseCost) {
        this.baseCost = baseCost;
    }

    public int getBaseCost() {
        return baseCost;
    }
}
