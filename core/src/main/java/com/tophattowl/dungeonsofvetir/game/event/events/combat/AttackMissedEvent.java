package com.tophattowl.dungeonsofvetir.game.event.events.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.event.events.Event;

public record AttackMissedEvent(
    Entity attacker,
    Entity target
) implements Event {
}
