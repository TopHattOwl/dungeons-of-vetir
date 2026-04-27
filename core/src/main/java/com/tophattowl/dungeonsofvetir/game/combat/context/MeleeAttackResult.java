package com.tophattowl.dungeonsofvetir.game.combat.context;


import com.tophattowl.dungeonsofvetir.game.items.Item;

public class MeleeAttackResult extends AttackResult{
    protected boolean countered = false;
    protected Item usedWeapon = null;

    public MeleeAttackResult() {}

    public void countered() {
        this.countered = true;
    }

    public void setUsedWeapon(Item usedWeapon) {
        this.usedWeapon = usedWeapon;
    }

    public boolean isCountered() {
        return countered;
    }

    public Item getUsedWeapon() {
        return usedWeapon;
    }
}
