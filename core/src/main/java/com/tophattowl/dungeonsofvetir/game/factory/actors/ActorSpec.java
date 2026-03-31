package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs.ActorComponentSpec;
import com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs.BodyComponentSpec;

import java.util.List;

public record ActorSpec(
    ActorId actorId,
    String name,
    Faction faction,
    int spawnCost,
    BodyComponentSpec bodySpec,
    List<ActorComponentSpec<?>> baseSpecs,
    List<ActorComponentSpec<?>> postBodySpecs
) {}
