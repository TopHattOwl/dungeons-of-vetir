package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.monsters.NaturalWeapon;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.combat.damage.DamageInstance;
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

                new BodySpec(
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
                    new AiSpec(
                        5,
                        1,
                        4,
                        2
                    ),
                    new FovSpec(8),
                    new HealthSpec(120),
                    new OffensiveStatsSpec(
                        20,
                        1.0f,
                        1.0f,
                        1.0f,
                        50
                    ),
                    new RenderableSpec("iron_worm", 0),
                    new TimeValueSpec()
                ),

                // post body component specs
                List.of(
                    new DefensiveStatsSpec(
                        50,
                        0.05f,
                        0.05f
                    ),
                    new NaturalWeaponsSpec(Map.of(
                        "tail", new NaturalWeapon(DamageType.SLASHING, 0.5f, Map.of(
                                ElementType.PHYSICAL, 13
                        )),
                        "head", new NaturalWeapon(DamageType.PIERCING, 1.0f, Map.of(
                                ElementType.PHYSICAL, 12,
                                ElementType.POISON, 5
                        ))
                    ))
                )
            )
        );

        register(
            new ActorSpec(
                ActorId.SCAVENGER,
                "Scavenger",
                Faction.LOOTER,
                15,

                new BodySpec(BodyTemplate.HUMANOID),

                // base component specs
                List.of(
                    new AiSpec(
                        0,
                        -1,
                        0,
                        2
                    ),
                    new FovSpec(11),
                    new HealthSpec(150),
                    new OffensiveStatsSpec(
                        15,
                        1.1f,
                        1.1f,
                        1.05f,
                        40
                    ),
                    new RenderableSpec("scavenger", 0),
                    new TimeValueSpec()
                ),

                // post body component specs
                List.of(
                    new DefensiveStatsSpec(
                        75,
                        0.1f,
                        0.05f
                    ),
                    new EquipmentSpec()
                )
            )
        );
    }
}
