package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.DerivedStatsComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.stats.CombatStat;

/**
 * Restores HP and/or body-part condition. Amounts may be flat and/or read from a derived stat
 * (natural regeneration reads {@code HP_RESTORATION} / {@code BODY_PART_RESTORATION}).
 * <p>
 * When the effect has no bound part, part condition is restored across the whole body.
 */
public record HealAction(
    int hp,
    int condition,
    CombatStat hpStat,
    CombatStat conditionStat
) implements EffectAction {

    @Override
    public void apply(EffectContext ctx, Entity target, StatusEffect instance) {
        if (target == null) return;

        int hpAmount = hp + statValue(target, hpStat);
        if (hpAmount > 0) {
            HealthComponent health = target.getComponent(HealthComponent.class);
            if (health != null) health.heal(hpAmount);
        }

        int conditionAmount = condition + statValue(target, conditionStat);
        if (conditionAmount <= 0) return;

        BodyPart part = instance == null ? null : instance.part();
        if (part != null) {
            part.heal(conditionAmount);
            return;
        }

        BodyComponent body = target.getComponent(BodyComponent.class);
        if (body != null) {
            for (BodyPart bodyPart : body.bodyParts) bodyPart.heal(conditionAmount);
        }
    }

    private static int statValue(Entity target, CombatStat stat) {
        if (stat == null) return 0;
        DerivedStatsComponent derived = target.getComponent(DerivedStatsComponent.class);
        return derived == null ? 0 : derived.getInt(stat);
    }
}
