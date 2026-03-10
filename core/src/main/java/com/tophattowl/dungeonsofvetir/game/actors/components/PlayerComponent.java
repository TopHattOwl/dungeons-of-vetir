package com.tophattowl.dungeonsofvetir.game.actors.components;


import com.tophattowl.dungeonsofvetir.game.ECS.Component;

public class PlayerComponent implements Component {
    public boolean isPlayersTurn;

    public PlayerComponent() {
        isPlayersTurn = false;
    }
}
