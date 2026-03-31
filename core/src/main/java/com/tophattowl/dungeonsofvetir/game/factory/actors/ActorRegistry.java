package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActorRegistry {

    private static final Map<ActorId, ActorSpec> registry = new HashMap<>();

    private static void register(ActorSpec spec) {
        registry.put(spec.actorId(), spec);
    }

    public static ActorSpec get(ActorId actorId) {
        ActorSpec spec = registry.get(actorId);
        if (spec == null) {
            throw new IllegalArgumentException("No template found for Actor with id %s" + actorId);
        }
        return spec;
    }

    static {
        register(
            new ActorSpec(
                ActorId.IRON_WORM,
                "Iron worm",
                Faction.MONSTER,
                10,

                new BodyComponentSpec(
                    BodyTemplate.WORM,
                    Map.of(
                        "head", 10,
                        "segment1", 15,
                        "segment2", 15,
                        "segment3", 15,
                        "tail", 12
                    )
                ),

                // base component specs
                List.of(
                    new AiComponentSpec(
                        5,
                        1,
                        4,
                        2
                    ),
                    new FovComponentSpec(8),
                    new HealthComponentSpec(120),
                    new OffensiveStatsComponentSpec(
                        20,
                        1.0f,
                        1.0f,
                        1.0f,
                        50
                    ),
                    new RenderableComponentSpec("iron_worm", 0),
                    new TimeValueComponentSpec()
                ),

                // post body component specs
                List.of(
                    new DefensiveStatsComponentSpec(
                        50,
                        0.05f,
                        0.05f
                    )
                )
            )
        );

        register(
            new ActorSpec(
                ActorId.SCAVENGER,
                "Scavenger",
                Faction.LOOTER,
                15,

                new BodyComponentSpec(BodyTemplate.HUMANOID),

                // base component specs
                List.of(
                    new AiComponentSpec(
                        0,
                        -1,
                        0,
                        2
                    ),
                    new FovComponentSpec(11),
                    new HealthComponentSpec(150),
                    new OffensiveStatsComponentSpec(
                        15,
                        1.1f,
                        1.1f,
                        1.05f,
                        40
                    ),
                    new RenderableComponentSpec("scavenger", 0),
                    new TimeValueComponentSpec()
                ),

                // post body component specs
                List.of(
                    new DefensiveStatsComponentSpec(
                        75,
                        0.1f,
                        0.05f
                    ),
                    new EquipmentComponentSpec()
                )
            )
        );
    }
}
