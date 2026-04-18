package com.tophattowl.dungeonsofvetir.game.ECS;

// for components that need the owner Entity
public interface OwnedComponent extends Component {
    void setOwner(Entity owner);
}
