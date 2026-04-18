package com.tophattowl.dungeonsofvetir.game.event.events.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.event.events.Event;

public record BodyPartDestroyedEvent(
    Entity target,
    BodyPart bodyPart
) implements Event {}
