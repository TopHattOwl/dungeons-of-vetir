package com.tophattowl.dungeonsofvetir.game.combat.context;

import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;

import java.util.ArrayList;
import java.util.List;

/**
 * Output for AttackCalculators
 */
public abstract class AttackResult {
    protected boolean missed = false;
    protected boolean blocked = false;
    protected BodyPart bodyPart = null;
    protected List<Damage> damages = new ArrayList<>();

    public AttackResult() {}

    public void addDamage(Damage damage) {
        damages.add(damage);
    }

    public List<Damage> getDamages() {
        return damages;
    }

    public void missed() {
        this.missed = true;
    }

    public void blocked() {
        this.blocked = true;
    }

    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    public boolean isMissed() {
        return missed;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public BodyPart getBodyPart() {
        return bodyPart;
    }

    @Override
    public String toString() {
        return "missed=" + missed
            + ", blocked=" + blocked
            + ", damages=" + damages;
    }
}
