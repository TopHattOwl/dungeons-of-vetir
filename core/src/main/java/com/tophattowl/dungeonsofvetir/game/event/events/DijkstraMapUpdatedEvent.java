package com.tophattowl.dungeonsofvetir.game.event.events;

import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapType;

public record DijkstraMapUpdatedEvent(DijkstraMapType mapType) implements Event {}
