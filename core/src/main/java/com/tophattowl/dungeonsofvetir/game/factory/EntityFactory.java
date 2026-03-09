package com.tophattowl.dungeonsofvetir.game.factory;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.*;
import com.tophattowl.dungeonsofvetir.game.world.Level;

public class EntityFactory {

    public static Entity makeEnemy() {
        return makeEnemy(0, 0);
    }

    public static Entity makeEnemy(int x, int y) {
        Entity entity = new Entity();
        entity.addComponent(new PositionComponent(x, y))
            .addComponent(new RenderableComponent("enemy"))
            .addComponent(new TimeValueComponent())
            .addComponent(new FovComponent(8, Level.WIDTH, Level.HEIGHT));

        entity.addComponent(new IdentityComponent("monster" + entity.id));

        return entity;
    }

    public static Entity makePlayer() {
        Entity player = new Entity();
        player.addComponent(new PositionComponent(0, 0))
            .addComponent(new RenderableComponent("player", 10))
            .addComponent(new TimeValueComponent())
            .addComponent(new FovComponent(8, Level.WIDTH, Level.HEIGHT))
            .addComponent(new PlayerComponent())
            .addComponent(new IdentityComponent("player"));

        return player;
    }

}
