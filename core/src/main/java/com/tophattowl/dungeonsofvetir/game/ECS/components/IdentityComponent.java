package com.tophattowl.dungeonsofvetir.game.ECS.components;

public class IdentityComponent implements Component{
    public String name;

    public IdentityComponent(String name) {
        this.name = name;
    }
}
