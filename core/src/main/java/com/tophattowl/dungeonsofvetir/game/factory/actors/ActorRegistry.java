package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;

import java.util.HashMap;
import java.util.Map;

public class ActorRegistry {

    private static final Map<ActorId, ActorTemplate> registry = new HashMap<>();

    private static void register(ActorTemplate template) {
        registry.put(template.actorId, template);
    }

    public static ActorTemplate get(ActorId actorId) {
        ActorTemplate template = registry.get(actorId);
        if (template == null) {
            throw new IllegalArgumentException("No template found for Actor with id %s" + actorId);
        }
        return template;
    }

    static {
        register(
            new ActorTemplate.Builder(ActorId.IRON_WORM, "Iron Worm")
                .spawnCost(5)
                .spriteId("iron_worm")
                .bodyTemplate(BodyTemplate.WORM)
                .baseSpeed(1.0f)
                .visionRange(7)
                .maxHp(115)
                .baseDamage(20)
                .weaponDamageModifier(0.9f)
                .build()
        );
    }
}
