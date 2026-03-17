package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;

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
            new ActorTemplate.Builder(ActorId.IRON_WORM, "Iron Worm", Faction.MONSTER)
                .spawnCost(5)
                .spriteId("iron_worm")
                .bodyTemplate(BodyTemplate.WORM)
                .visionRange(7)
                .maxHp(115)
                .baseDamage(12)
                .weaponDamageModifier(0.9f)
                .build()
        );
        register(
            new ActorTemplate.Builder(ActorId.SCAVENGER, "Scavenger", Faction.MONSTER)
                .spawnCost(8)
                .baseDamage(10)
                .accuracy(1.0f)
                .maxHp(145)
                .visionRange(11)
                .build()
        );
    }
}
