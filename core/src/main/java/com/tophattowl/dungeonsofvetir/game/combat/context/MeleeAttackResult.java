package com.tophattowl.dungeonsofvetir.game.combat.context;


public class MeleeAttackResult extends AttackResult{
    protected boolean countered = false;

    public MeleeAttackResult() {}

    public void countered() {
        this.countered = true;
    }

    public boolean isCountered() {
        return countered;
    }
}
