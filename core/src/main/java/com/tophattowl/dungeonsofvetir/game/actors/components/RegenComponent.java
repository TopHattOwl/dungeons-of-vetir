package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.OwnedComponent;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.TurnPassedEvent;

public class RegenComponent implements OwnedComponent {
    private Entity owner;
    private int turnCounter = 0;

    // how many turns one tick takes
    public int tickThreshold;

    // the ratio for body part regen
    public float bodyPartHealEfficiency;

    public int hpPerTick;

    public RegenComponent(int hpPerTick, int tickThreshold, float bodyPartHealEfficiency) {
        this.hpPerTick = hpPerTick;
        this.tickThreshold = tickThreshold;
        this.bodyPartHealEfficiency = bodyPartHealEfficiency;
        EventBus.on(TurnPassedEvent.class, this::onTurnPassed);
    }

    private void onTurnPassed(TurnPassedEvent event) {
        turnCounter++;

        if (turnCounter == tickThreshold) {
            regen();
            turnCounter = 0;
        }
    }

    private void regen() {
        HealthComponent hpComp = owner.getComponent(HealthComponent.class);
        if (hpComp == null) {
            throw new NullPointerException("[RegenComponent] Health comp is null for entity: " + owner);
        }

        BodyComponent bodyComp = owner.getComponent(BodyComponent.class);
        if (bodyComp == null) {
            throw new NullPointerException("[RegenComponent] Body comp is null for entity: " + owner);
        }

        hpComp.heal(hpPerTick);

        int partHeal = Math.max(1, (int) (hpPerTick * bodyPartHealEfficiency));
        for (BodyPart bodyPart : bodyComp.bodyParts) {
            bodyPart.heal(partHeal);
        }
    }

    @Override
    public void setOwner(Entity owner) {
        this.owner = owner;
    }
}
