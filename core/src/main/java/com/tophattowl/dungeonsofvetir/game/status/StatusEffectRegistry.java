package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.stats.CombatStat;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.world.Capability;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * All status effect definitions. Magnitudes are first-pass placeholders.
 */
public final class StatusEffectRegistry {

    private static final Map<StatusEffectId, StatusEffectDefinition> DEFINITIONS =
        new EnumMap<>(StatusEffectId.class);

    private StatusEffectRegistry() {}

    public static StatusEffectDefinition get(StatusEffectId id) {
        StatusEffectDefinition definition = DEFINITIONS.get(id);
        if (definition == null) {
            throw new IllegalStateException("No definition for status effect " + id);
        }
        return definition;
    }

    private static void register(StatusEffectDefinition definition) {
        DEFINITIONS.put(definition.id(), definition);
    }

    private static StatusEffectDefinition of(StatusEffectId id, String name, EffectKind kind,
                                             EffectPersistence persistence, StackingPolicy stacking,
                                             TargetScope scope, List<Modifier> modifiers,
                                             List<Capability> denials, List<EffectAction> onApply,
                                             List<PeriodicEffect> periodics) {
        return new StatusEffectDefinition(id, name, kind, persistence, stacking, scope,
            modifiers, denials, onApply, periodics);
    }

    static {
        register(of(
            StatusEffectId.STUNNED, "Stunned", EffectKind.AFFLICTION,
            EffectPersistence.TIMED, StackingPolicy.REFRESH, TargetScope.ENTITY,
            List.of(Modifier.percent(CombatStat.DODGE_CHANCE, -0.5f)),
            List.of(Capability.MOVE, Capability.ATTACK),
            List.of(), List.of()
        ));

        register(of(
            StatusEffectId.BURNING, "Burning", EffectKind.AFFLICTION,
            EffectPersistence.TIMED, StackingPolicy.INTENSITY, TargetScope.ENTITY,
            List.of(), List.of(),
            List.of(new DamageAction(2, 0, ElementType.FIRE, DamageType.CRUSHING, false)),
            List.of(new PeriodicEffect(2, List.of(
                new DamageAction(4, 0, ElementType.FIRE, DamageType.CRUSHING, false))))
        ));

        register(of(
            StatusEffectId.BLEEDING, "Bleeding", EffectKind.AFFLICTION,
            EffectPersistence.TIMED, StackingPolicy.INTENSITY, TargetScope.BODY_PART,
            List.of(), List.of(), List.of(),
            List.of(new PeriodicEffect(2, List.of(
                new DamageAction(3, 4, ElementType.PHYSICAL, DamageType.SLASHING, true))))
        ));

        register(of(
            StatusEffectId.POISONED, "Poisoned", EffectKind.AFFLICTION,
            EffectPersistence.TIMED, StackingPolicy.INTENSITY, TargetScope.ENTITY,
            List.of(), List.of(), List.of(),
            List.of(new PeriodicEffect(3, List.of(
                new DamageAction(4, 0, ElementType.POISON, DamageType.PIERCING, true))))
        ));

        register(of(
            StatusEffectId.REGENERATING, "Regenerating", EffectKind.BOON,
            EffectPersistence.PERSISTENT, StackingPolicy.REFRESH, TargetScope.ENTITY,
            List.of(), List.of(), List.of(),
            List.of(new PeriodicEffect(5, List.of(
                new HealAction(0, 0, CombatStat.HP_RESTORATION, CombatStat.BODY_PART_RESTORATION))))
        ));

        register(of(
            StatusEffectId.WOUNDED, "Wounded", EffectKind.AFFLICTION,
            EffectPersistence.PERSISTENT, StackingPolicy.REFRESH, TargetScope.ENTITY,
            List.of(Modifier.percent(CombatStat.HP_RESTORATION, -0.5f)),
            List.of(), List.of(), List.of()
        ));

        register(of(
            StatusEffectId.CONCUSSED, "Concussed", EffectKind.AFFLICTION,
            EffectPersistence.PERSISTENT, StackingPolicy.REFRESH, TargetScope.ENTITY,
            List.of(Modifier.percent(CombatStat.ACCURACY, -0.15f)),
            List.of(), List.of(), List.of()
        ));

        register(of(
            StatusEffectId.CRIPPLED, "Crippled", EffectKind.AFFLICTION,
            EffectPersistence.PERSISTENT, StackingPolicy.REFRESH, TargetScope.ENTITY,
            List.of(
                Modifier.percent(CombatStat.ACCURACY, -0.1f),
                Modifier.percent(CombatStat.MAIN_HAND_EFFICIENCY, -0.2f)),
            List.of(), List.of(), List.of()
        ));

        register(of(
            StatusEffectId.RECENTLY_WOUNDED, "Recently wounded", EffectKind.NEUTRAL,
            EffectPersistence.TIMED, StackingPolicy.REFRESH, TargetScope.ENTITY,
            List.of(), List.of(), List.of(), List.of()
        ));

        register(of(
            StatusEffectId.EXERTED, "Exerted", EffectKind.NEUTRAL,
            EffectPersistence.TIMED, StackingPolicy.REFRESH, TargetScope.ENTITY,
            List.of(), List.of(), List.of(), List.of()
        ));
    }
}
