package com.tophattowl.dungeonsofvetir.game.event.events.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.event.events.Event;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffect;

public record StatusEffectAppliedEvent(Entity entity, StatusEffect effect) implements Event {
}
