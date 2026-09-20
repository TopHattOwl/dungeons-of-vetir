package com.tophattowl.dungeonsofvetir.game.debug;

import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapType;

/**
 * Display-agnostic hook the debug console uses to drive the Dijkstra overlay.
 * Implemented by the display layer so the console model stays free of display imports.
 */
public interface DijkstraOverlayControl {
    void setMapType(DijkstraMapType mapType);
    DijkstraMapType getMapType();
    boolean isEnabled();
    void toggle();
}
