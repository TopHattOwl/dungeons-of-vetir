package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.badlogic.gdx.utils.Disposable;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.OwnedComponent;
import com.tophattowl.dungeonsofvetir.game.action.ActionType;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.event.EventSubscriptions;
import com.tophattowl.dungeonsofvetir.game.event.events.ActionCompletedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.TurnPassedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.combat.MeleeAttackHitEvent;

import java.util.Set;

public class RegenComponent implements OwnedComponent, Disposable {
    private static final Set<ActionType> SAFE_ACTIONS = Set.of(
        ActionType.PASS, ActionType.MOVE, ActionType.PICKUP, ActionType.DROP, ActionType.NONE
    );

    private static final int DAMAGE_COOLDOWN = 12;
    private static final int HEAVY_ACTION_COOLDOWN = 8;

    public int hpPerTick;
    public int tickThreshold;
    public float bodyPartHealEfficiency;

    private Entity owner;
    private int turnCounter = 0;
    private int cooldownTurns = 0;

    private final EventSubscriptions eventSubs = new EventSubscriptions();

    public RegenComponent(int hpPerTick, int tickThreshold, float bodyPartHealEfficiency) {
        this.hpPerTick = hpPerTick;
        this.tickThreshold = tickThreshold;
        this.bodyPartHealEfficiency = bodyPartHealEfficiency;
    }

    private void onTurnPassed(TurnPassedEvent event) {
        if (cooldownTurns > 0) {
            cooldownTurns--;
            return;
        }

        turnCounter++;
        if (turnCounter >= tickThreshold) {
            regen();
            turnCounter = 0;
        }
    }

    private void onActionCompleted(ActionCompletedEvent event) {
        if (!event.entity().equals(owner)) return;
        if (!SAFE_ACTIONS.contains(event.action().getActionType())) {
            cooldownTurns = Math.max(cooldownTurns, HEAVY_ACTION_COOLDOWN);
            turnCounter = 0;
        }
    }

    private void onDamaged(MeleeAttackHitEvent event) {
        if (!event.target().equals(owner)) return;
        cooldownTurns = Math.max(cooldownTurns, DAMAGE_COOLDOWN);
        turnCounter = 0;
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
        eventSubs.on(TurnPassedEvent.class, this::onTurnPassed);
        eventSubs.on(ActionCompletedEvent.class, this::onActionCompleted);
        eventSubs.on(MeleeAttackHitEvent.class, this::onDamaged);
    }

    @Override
    public void dispose() {
        eventSubs.unsubscribeAll();
    }
}
