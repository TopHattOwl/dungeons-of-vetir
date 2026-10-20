package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;

/**
 * A live status effect on an entity. Runtime state only: the static behaviour lives in the
 * {@link StatusEffectDefinition} looked up by {@link #id()}.
 * <p>
 * {@code remainingTurns} is {@code > 0} for timed effects and {@code < 0} for untimed
 * (persistent/permanent) ones. {@code periodicTimers} mirrors the definition's periodic list.
 */
public class StatusEffect {

    private final StatusEffectId id;
    private final BodyPart part;
    private final String sourceId;
    private int stacks;
    private int remainingTurns;
    private final int[] periodicTimers;

    public StatusEffect(StatusEffectId id, BodyPart part, String sourceId,
                        int stacks, int remainingTurns, int[] periodicTimers) {
        this.id = id;
        this.part = part;
        this.sourceId = sourceId;
        this.stacks = Math.max(1, stacks);
        this.remainingTurns = remainingTurns;
        this.periodicTimers = periodicTimers;
    }

    public StatusEffectId id() {
        return id;
    }

    public BodyPart part() {
        return part;
    }

    public String sourceId() {
        return sourceId;
    }

    public int stacks() {
        return stacks;
    }

    public void addStacks(int amount) {
        stacks = Math.max(1, stacks + amount);
    }

    public int remainingTurns() {
        return remainingTurns;
    }

    public void refreshDuration(int turns) {
        if (turns > 0) {
            remainingTurns = turns;
        }
    }

    public boolean isTimed() {
        return remainingTurns > 0;
    }

    public boolean expired() {
        return remainingTurns == 0;
    }

    /**
     * Ages a timed effect one turn.
     */
    public void tickDuration() {
        if (remainingTurns > 0) {
            remainingTurns--;
        }
    }

    public int[] periodicTimers() {
        return periodicTimers;
    }

    public void resetPeriodicTimer(int index, int interval) {
        periodicTimers[index] = interval;
    }
}
