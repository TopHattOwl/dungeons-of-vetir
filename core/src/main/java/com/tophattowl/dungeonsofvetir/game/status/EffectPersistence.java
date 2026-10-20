package com.tophattowl.dungeonsofvetir.game.status;

/**
 * How long an effect lasts.
 * <ul>
 *   <li>{@code TIMED} — a fixed number of world turns.</li>
 *   <li>{@code PERSISTENT} — until its state predicate stops holding or it is removed.</li>
 *   <li>{@code PERMANENT} — only removed by special means (corruption, scars).</li>
 * </ul>
 */
public enum EffectPersistence {
    TIMED,
    PERSISTENT,
    PERMANENT,
}
