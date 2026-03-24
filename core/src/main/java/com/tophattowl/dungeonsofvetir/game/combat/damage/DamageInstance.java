package com.tophattowl.dungeonsofvetir.game.combat.damage;

import com.tophattowl.dungeonsofvetir.game.combat.ElementType;

public class DamageInstance {
    private int baseAmount;
    private ElementType elementType;

    public DamageInstance(int baseAmount, ElementType elementType) {
        this.baseAmount = baseAmount;
        this.elementType = elementType;
    }

    public int getBaseAmount() {
        return baseAmount;
    }

    public ElementType getElementType() {
        return elementType;
    }
}
