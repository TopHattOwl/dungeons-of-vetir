package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;

/**
 * Deals damage to HP and, when the effect is bound to a body part, to that part's condition.
 * <p>
 * In phase 3B this applies raw; phase 3C routes it through the damage resolver (protection,
 * resistance, block) unless {@code bypassProtection} is set (bleeding bypasses armor).
 */
public record DamageAction(
    int hp,
    int condition,
    ElementType element,
    DamageType damageType,
    boolean bypassProtection
) implements EffectAction {

    @Override
    public void apply(EffectContext ctx, Entity target, StatusEffect instance) {
        if (target == null) return;

        if (hp > 0) {
            HealthComponent health = target.getComponent(HealthComponent.class);
            if (health != null) health.takeDamage(hp);
        }

        BodyPart part = instance == null ? null : instance.part();
        if (part != null && condition > 0) {
            part.takeDamage(condition);
        }
    }
}
