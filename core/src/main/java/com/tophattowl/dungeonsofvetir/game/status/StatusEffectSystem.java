package com.tophattowl.dungeonsofvetir.game.status;

import com.badlogic.gdx.utils.Disposable;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.status.StatusEffectAppliedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.status.StatusEffectExpiredEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.status.StatusEffectRemovedEvent;
import com.tophattowl.dungeonsofvetir.game.system.TurnSystem;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.Constraint;
import com.tophattowl.dungeonsofvetir.game.world.ConstraintComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.WorldContext;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

/**
 * Owns the lifetime of status effects.
 * <p>
 * At world turn end it ticks durations/stacks, fires periodics, expires finished effects,
 * evaluates the threshold/state rules, and compiles capability denials into each entity's {@link ConstraintComponent} <br>
 * {@link #apply} and {@link #remove} run the same steps immediately
 * so procs land the same turn.
 */
public class StatusEffectSystem implements TurnSystem, Disposable {

    private final GameWorld world;
    private final Random rng = new Random();

    public StatusEffectSystem(GameWorld world) {
        this.world = world;
    }

    // ------------------------------------------------------------------
    // apply / remove (immediate)
    // ------------------------------------------------------------------

    public void apply(Entity target, StatusEffectId id, String sourceId, int stacks, int durationTurns) {
        apply(target, id, null, sourceId, stacks, durationTurns);
    }

    public void apply(Entity target, StatusEffectId id, BodyPart part,
                      String sourceId, int stacks, int durationTurns) {
        if (target == null) return;

        StatusEffectDefinition definition = StatusEffectRegistry.get(id);
        StatusEffectComponent component = ensureComponent(target);
        StatusEffect existing = component.find(id, part);

        if (existing != null && definition.stacking() != StackingPolicy.INDEPENDENT) {
            if (definition.stacking() == StackingPolicy.INTENSITY) {
                existing.addStacks(stacks);
            }
            existing.refreshDuration(effectiveDuration(definition, durationTurns));
        } else {
            StatusEffect effect = create(definition, part, sourceId, stacks, durationTurns);
            component.add(effect);
            fireActions(definition.onApply(), target, effect);
            EventBus.emit(new StatusEffectAppliedEvent(target, effect));
        }

        update(target);
    }

    public void remove(Entity target, StatusEffectId id, String reason) {
        if (target == null) return;
        StatusEffectComponent component = target.getComponent(StatusEffectComponent.class);
        if (component == null) return;

        List<StatusEffect> removed = new ArrayList<>();
        for (StatusEffect effect : component.effects()) {
            if (effect.id() == id) removed.add(effect);
        }
        for (StatusEffect effect : removed) {
            component.remove(effect);
            EventBus.emit(new StatusEffectRemovedEvent(target, effect, reason));
        }
        if (!removed.isEmpty()) update(target);
    }

    // ------------------------------------------------------------------
    // turn processing
    // ------------------------------------------------------------------

    @Override
    public void process(WorldContext context) {
        List<Entity> effectHolders = world.query(StatusEffectComponent.class);

        List<Entity> dirty = new ArrayList<>();
        for (Entity entity : effectHolders) {
            if (tickEntity(entity)) dirty.add(entity);
        }

        for (Entity entity : world.getAllEntities()) {
            if (applyStateRules(entity)) dirty.add(entity);
        }

        for (Entity entity : world.query(StatusEffectComponent.class)) {
            compileConstraints(entity);
        }
        for (Entity entity : dirty) {
            world.stats().recompute(entity);
        }
    }

    /**
     * Ticks one entity's effects; returns true if any continuous contribution changed.
     */
    private boolean tickEntity(Entity entity) {
        StatusEffectComponent component = entity.getComponent(StatusEffectComponent.class);
        boolean changed = false;

        List<StatusEffect> snapshot = new ArrayList<>(component.effects());
        for (StatusEffect effect : snapshot) {
            StatusEffectDefinition definition = StatusEffectRegistry.get(effect.id());

            firePeriodics(entity, effect, definition);

            effect.tickDuration();
            if (effect.expired()) {
                component.remove(effect);
                EventBus.emit(new StatusEffectExpiredEvent(entity, effect));
                changed = true;
            }
        }

        component.removeDestroyedPartEffects();
        return changed;
    }

    private void firePeriodics(Entity entity, StatusEffect effect, StatusEffectDefinition definition) {
        int[] timers = effect.periodicTimers();
        List<PeriodicEffect> periodics = definition.periodics();
        for (int i = 0; i < periodics.size(); i++) {
            timers[i]--;
            if (timers[i] <= 0) {
                fireActions(periodics.get(i).actions(), entity, effect);
                effect.resetPeriodicTimer(i, periodics.get(i).intervalTurns());
            }
        }
    }

    private void fireActions(List<EffectAction> actions, Entity entity, StatusEffect effect) {
        if (actions.isEmpty()) return;
        EffectContext ctx = new EffectContext(world, rng);
        for (EffectAction action : actions) {
            action.apply(ctx, entity, effect);
        }
    }

    /**
     * Ensures every state effect whose predicate holds is present, and removes ones whose
     * predicate no longer holds. Never resets an existing instance's timers or stacks.
     */
    private boolean applyStateRules(Entity entity) {
        if (entity.getComponent(HealthComponent.class) == null) return false;

        boolean changed = false;
        for (StateRule rule : StateRuleRegistry.rules()) {
            boolean holds = rule.applies().test(world, entity);
            StatusEffectComponent component = entity.getComponent(StatusEffectComponent.class);

            if (holds) {
                if (component == null) {
                    component = new StatusEffectComponent();
                    entity.addComponent(component);
                }
                if (component.find(rule.effect(), null) == null) {
                    StatusEffectDefinition definition = StatusEffectRegistry.get(rule.effect());
                    component.add(create(definition, null, "state", rule.stacks(), -1));
                    EventBus.emit(new StatusEffectAppliedEvent(entity, component.find(rule.effect(), null)));
                    changed = true;
                }
            } else if (component != null && component.has(rule.effect())) {
                component.removeAll(rule.effect());
                changed = true;
            }
        }
        return changed;
    }

    private void update(Entity entity) {
        compileConstraints(entity);
        world.stats().recompute(entity);
    }

    private void compileConstraints(Entity entity) {
        StatusEffectComponent component = entity.getComponent(StatusEffectComponent.class);
        EnumSet<Capability> denials = component == null
            ? EnumSet.noneOf(Capability.class)
            : component.activeDenials();

        ConstraintComponent constraints = entity.getComponent(ConstraintComponent.class);
        if (denials.isEmpty()) {
            if (constraints != null) constraints.clear();
            return;
        }

        if (constraints == null) {
            constraints = new ConstraintComponent();
            entity.addComponent(constraints);
        }
        List<Constraint> compiled = new ArrayList<>();
        for (Capability capability : denials) {
            compiled.add(Constraint.of(capability, "status effect"));
        }
        constraints.setAll(compiled);
    }

    private StatusEffectComponent ensureComponent(Entity entity) {
        StatusEffectComponent component = entity.getComponent(StatusEffectComponent.class);
        if (component == null) {
            component = new StatusEffectComponent();
            entity.addComponent(component);
            DebugLogger.log(DebugLogger.Category.STATUS, "StatusEffectSystem",
                "created status component for " + entity);
        }
        return component;
    }

    private StatusEffect create(StatusEffectDefinition definition, BodyPart part,
                                String sourceId, int stacks, int durationTurns) {
        List<PeriodicEffect> periodics = definition.periodics();
        int[] timers = new int[periodics.size()];
        for (int i = 0; i < periodics.size(); i++) {
            timers[i] = periodics.get(i).intervalTurns();
        }
        return new StatusEffect(definition.id(), part, sourceId, stacks,
            effectiveDuration(definition, durationTurns), timers);
    }

    private int effectiveDuration(StatusEffectDefinition definition, int durationTurns) {
        if (durationTurns > 0) return durationTurns;
        return definition.persistence() == EffectPersistence.TIMED ? 1 : -1;
    }

    @Override
    public void dispose() {
        // no resources held
    }
}
