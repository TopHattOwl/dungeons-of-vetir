package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.stats.ModifierSource;
import com.tophattowl.dungeonsofvetir.game.world.Capability;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The status effects currently on an entity. Continuous contributions (modifiers) are exposed
 * to the stat aggregator; capability denials are compiled into the entity's constraints by the
 * status system.
 */
public class StatusEffectComponent implements Component, ModifierSource {

    private final List<StatusEffect> effects = new ArrayList<>();

    public List<StatusEffect> effects() {
        return effects;
    }

    public StatusEffect find(StatusEffectId id, BodyPart part) {
        for (StatusEffect effect : effects) {
            if (effect.id() == id && effect.part() == part) {
                return effect;
            }
        }
        return null;
    }

    public boolean has(StatusEffectId id) {
        for (StatusEffect effect : effects) {
            if (effect.id() == id) return true;
        }
        return false;
    }

    public void add(StatusEffect effect) {
        effects.add(effect);
    }

    public void remove(StatusEffect effect) {
        effects.remove(effect);
    }

    public void removeAll(StatusEffectId id) {
        effects.removeIf(effect -> effect.id() == id);
    }

    /**
     * Drops part-scoped effects whose part has been destroyed.
     */
    public void removeDestroyedPartEffects() {
        effects.removeIf(effect -> effect.part() != null && effect.part().isDestroyed());
    }

    public EnumSet<Capability> activeDenials() {
        EnumSet<Capability> denials = EnumSet.noneOf(Capability.class);
        for (StatusEffect effect : effects) {
            denials.addAll(StatusEffectRegistry.get(effect.id()).denials());
        }
        return denials;
    }

    @Override
    public void collectModifiers(List<Modifier> out) {
        for (StatusEffect effect : effects) {
            StatusEffectDefinition definition = StatusEffectRegistry.get(effect.id());
            boolean scales = definition.stacking() == StackingPolicy.INTENSITY;
            for (Modifier modifier : definition.modifiers()) {
                float value = scales ? modifier.value() * effect.stacks() : modifier.value();
                out.add(new Modifier(modifier.target(), modifier.operation(), value));
            }
        }
    }
}
