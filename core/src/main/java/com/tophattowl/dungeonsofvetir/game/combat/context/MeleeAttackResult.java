package com.tophattowl.dungeonsofvetir.game.combat.context;

import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;

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
