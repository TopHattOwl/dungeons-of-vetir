package com.tophattowl.dungeonsofvetir.game.event.events.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.event.events.Event;

public record MeleeAttackBlockedEvent(
    Entity attacker,
    Entity blocker
) implements Event {
}
