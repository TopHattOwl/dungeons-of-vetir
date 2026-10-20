package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.world.Capability;

import java.util.List;

/**
 * Static data describing a status effect. Instances are created from this via
 * {@link StatusEffectRegistry}.
 * <p>
 * Continuous contributions are {@code modifiers} (folded into stat aggregation) and
 * {@code denials} (compiled into the entity's constraints). Discrete contributions are
 * {@code onApply} (once, when applied) and {@code periodics} (repeating).
 */
public record StatusEffectDefinition(
    StatusEffectId id,
    String name,
    EffectKind kind,
    EffectPersistence persistence,
    StackingPolicy stacking,
    TargetScope scope,
    List<Modifier> modifiers,
    List<Capability> denials,
    List<EffectAction> onApply,
    List<PeriodicEffect> periodics
) {}
