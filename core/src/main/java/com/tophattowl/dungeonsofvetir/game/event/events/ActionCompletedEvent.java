package com.tophattowl.dungeonsofvetir.game.event.events;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;

public record ActionCompletedEvent(Entity entity, Action action) {
}
