package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.actors.monsters.NaturalWeapon;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs.*;
import com.tophattowl.dungeonsofvetir.game.stats.CombatStat;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;

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
                    new AttributesSpec(7, 4, 2, 0, 2),
                    new AiSpec(
                        5,
                        1,
                        4,
                        2
                    ),
                    new FovSpec(8),
                    new RenderableSpec("iron_worm", 0),
                    new TimeValueSpec()
                ),

                // post body component specs
                List.of(
                    new NaturalWeaponsSpec(Map.of(
                        "tail", new NaturalWeapon(DamageType.SLASHING, 0.5f, Map.of(
                                ElementType.PHYSICAL, 13
                        )),
                        "head", new NaturalWeapon(DamageType.PIERCING, 1.0f, Map.of(
                                ElementType.PHYSICAL, 10,
                                ElementType.POISON, 5
                        ))
                    ))
                )
            )
        );

        register(
            new ActorSpec(
                ActorId.FIRE_ANT,
                "Fire Ant",
                Faction.MONSTER,
                8,

                new BodySpec(BodyTemplate.INSECT),

                // base component specs
                List.of(
                    new AttributesSpec(3, 4, 0, 0, 0),
                    new AiSpec(
                        5,
                        4,
                        2,
                        0
                    ),
                    new FovSpec(9),
                    new RenderableSpec("fireant", 0),
                    new TimeValueSpec()
                ),

                // post body component specs
                List.of(
                    new NaturalWeaponsSpec(Map.of(
                        "leg 1", new NaturalWeapon(DamageType.SLASHING, 0.5f, Map.of(
                            ElementType.PHYSICAL, 13
                        )),
                        "leg 2", new NaturalWeapon(DamageType.SLASHING, 0.5f, Map.of(
                            ElementType.PHYSICAL, 13
                        )),
                        "head", new NaturalWeapon(DamageType.PIERCING, 1.0f, Map.of(
                            ElementType.PHYSICAL, 10,
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
                    new AttributesSpec(6, 8, 3, 0, 4),
                    new ModifiersSpec(
                        Modifier.percent(CombatStat.WEAPON_DAMAGE_MODIFIER, 0.1f),
                        Modifier.percent(CombatStat.MAIN_HAND_EFFICIENCY, 0.1f),
                        Modifier.percent(CombatStat.OFF_HAND_EFFICIENCY, 0.05f)
                    ),
                    new AiSpec(
                        0,
                        -1,
                        0,
                        2
                    ),
                    new FovSpec(11),
                    new RenderableSpec("scavenger", 0),
                    new TimeValueSpec()
                ),

                // post body component specs
                List.of(
                    new EquipmentSpec()
                )
            )
        );
    }
}
