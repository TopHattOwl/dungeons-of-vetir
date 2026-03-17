package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;

public class IdentityComponent implements Component {
    public String name;
    public ActorId actorId;
    public Faction faction;

    public IdentityComponent(String name, ActorId actorId,  Faction faction) {
        this.name = name;
        this.actorId = actorId;
        this.faction = faction;
    }
}
