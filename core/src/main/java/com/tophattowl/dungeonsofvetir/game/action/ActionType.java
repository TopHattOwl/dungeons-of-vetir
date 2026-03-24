package com.tophattowl.dungeonsofvetir.game.action;

public enum ActionType {
    NONE(0),

    PASS(100),
    MOVE(100),

    MELEE_ATTACK(100),

    PICKUP(50),
    DROP(50),
    INTERACT(100),
    USE_ITEM(80),
    EQUIP(50),
    UNEQUIP(50),
    SWAP_EQUIPMENT(100),
    ;

    private final int baseCost;

    ActionType(int baseCost) {
        this.baseCost = baseCost;
    }

    public int getBaseCost() {
        return baseCost;
    }
}
