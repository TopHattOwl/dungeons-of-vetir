package com.tophattowl.dungeonsofvetir.game.world;

/**
 * A transition declared by an authored map: a tile that leads to another place,
 * identified by its target kind and content key. Resolved into an {@link Exit} when
 * the zone is entered.
 */
public record DeclaredExit(Point tile, TransitionKind kind, PlaceKind targetKind, String targetKey) {
}
