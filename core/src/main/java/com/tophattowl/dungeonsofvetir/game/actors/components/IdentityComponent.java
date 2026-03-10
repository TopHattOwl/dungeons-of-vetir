package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;

public class IdentityComponent implements Component {
    public String name;
    public ActorId actorId;

    public IdentityComponent(String name, ActorId actorId) {
        this.name = name;
        this.actorId = actorId;
    }
}
