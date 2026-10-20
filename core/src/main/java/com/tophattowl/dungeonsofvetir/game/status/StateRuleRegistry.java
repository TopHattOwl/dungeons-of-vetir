package com.tophattowl.dungeonsofvetir.game.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.List;

/**
 * The threshold/state rules evaluated every world turn. These own only their own effect ids and
 * never touch applied effects.
 */
public final class StateRuleRegistry {

    private static final float WOUNDED_RATIO = 0.35f;
    private static final float PART_INCAPACITATED_RATIO = 0.3f;

    private static final List<StateRule> RULES = List.of(
        new StateRule(StatusEffectId.WOUNDED, 1, StateRuleRegistry::isWounded),
        new StateRule(StatusEffectId.CONCUSSED, 1, StateRuleRegistry::isConcussed),
        new StateRule(StatusEffectId.CRIPPLED, 1, StateRuleRegistry::isCrippled),
        new StateRule(StatusEffectId.REGENERATING, 1, StateRuleRegistry::isRegenerating)
    );

    private StateRuleRegistry() {}

    public static List<StateRule> rules() {
        return RULES;
    }

    private static boolean isWounded(GameWorld world, Entity entity) {
        HealthComponent health = entity.getComponent(HealthComponent.class);
        return health != null && health.maxHp > 0 && health.hp > 0
            && (float) health.hp / health.maxHp < WOUNDED_RATIO;
    }

    private static boolean isConcussed(GameWorld world, Entity entity) {
        BodyPart head = findPart(entity, BodyPartType.HEAD);
        return head != null && (head.isDestroyed() || ratio(head) < PART_INCAPACITATED_RATIO);
    }

    private static boolean isCrippled(GameWorld world, Entity entity) {
        BodyComponent body = entity.getComponent(BodyComponent.class);
        if (body == null) return false;
        for (BodyPart part : body.bodyParts) {
            if (part.role == BodyPartRole.LIMB && !part.isDestroyed()
                && ratio(part) < PART_INCAPACITATED_RATIO) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRegenerating(GameWorld world, Entity entity) {
        HealthComponent health = entity.getComponent(HealthComponent.class);
        if (health == null || health.hp >= health.maxHp) return false;

        StatusEffectComponent effects = entity.getComponent(StatusEffectComponent.class);
        if (effects != null
            && (effects.has(StatusEffectId.RECENTLY_WOUNDED) || effects.has(StatusEffectId.EXERTED))) {
            return false;
        }
        return true;
    }

    private static BodyPart findPart(Entity entity, BodyPartType type) {
        BodyComponent body = entity.getComponent(BodyComponent.class);
        if (body == null) return null;
        for (BodyPart part : body.bodyParts) {
            if (part.type == type) return part;
        }
        return null;
    }

    private static float ratio(BodyPart part) {
        return part.maxHp > 0 ? (float) part.hp / part.maxHp : 0f;
    }
}
