package com.tophattowl.dungeonsofvetir.game.event.events;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public record EntityMovedEvent(Entity entity, Point newPos) {}
