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
                .hunterDijkstraWeight(10)
                .looterDijkstraWeight(3)
                .monsterDijkstraWeight(-2)
                .naturalProtections("segment1", 10)
                .naturalProtections("segment2", 10)
                .naturalProtections("segment3", 10)
                .naturalProtections("head", 12)
                .naturalProtections("tail", 8)
                .build()
        );
        register(
            new ActorTemplate.Builder(ActorId.SCAVENGER, "Scavenger", Faction.LOOTER)
                .spawnCost(8)
                .baseDamage(10)
                .maxHp(145)
                .visionRange(11)
                .monsterDijkstraWeight(-2)
                .hunterDijkstraWeight(1)
                .looterDijkstraWeight(3)
                .build()
        );
    }
}
