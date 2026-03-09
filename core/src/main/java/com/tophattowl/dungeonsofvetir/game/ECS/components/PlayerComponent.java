package com.tophattowl.dungeonsofvetir.game.ECS.components;


public class PlayerComponent implements Component{
    public boolean isPlayersTurn;

    public PlayerComponent() {
        isPlayersTurn = false;
    }
}
