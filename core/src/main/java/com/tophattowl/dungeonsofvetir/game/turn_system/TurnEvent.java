package com.tophattowl.dungeonsofvetir.game.turn_system;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;

public class TurnEvent extends Entity {
    public static final int TURN_TIME_VALUE = 100;

    public TurnEvent(int timeValue) {
        addComponent(new TimeValueComponent(TURN_TIME_VALUE));
        addComponent(new IdentityComponent("Turn event", ActorId.TURN_EVENT, Faction.SPECIAL));
    }

    public TurnEvent() {
        this(0);
    }

    public void passTurn() {
        getComponent(TimeValueComponent.class).addTime(TURN_TIME_VALUE);
    }
}
