package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplateBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public class EntityFactory {

    public static Entity createEntity(ActorId actorId, GameWorld gameWorld) {
        return createEntity(actorId, gameWorld, new Point(0, 0));
    }

    public static Entity createEntity(ActorId actorId, GameWorld gameWorld, Point spawnPos) {
        ActorTemplate template = ActorRegistry.get(actorId);
        return buildFromTemplate(template, gameWorld, spawnPos);
    }

    public static Entity makePlayer() {
        Entity player = new Entity();
        player.addComponent(new PositionComponent(0, 0))
            .addComponent(new RenderableComponent("player", 10))
            .addComponent(new TimeValueComponent())
            .addComponent(new FovComponent(10, Level.WIDTH, Level.HEIGHT))
            .addComponent(new PlayerComponent())
            .addComponent(new IdentityComponent("player", ActorId.PLAYER))
            .addComponent(new HealthComponent(25))
            .addComponent(new OffensiveStatsComponent(
                30, 1.0f,
                1.0f, 1.0f,
                1.0f));

        return player;
    }

    private static Entity buildFromTemplate(ActorTemplate template, GameWorld gameWorld, Point spawnPos) {
        Entity entity = new Entity();

        entity.addComponent(new PositionComponent(spawnPos))
            .addComponent(new RenderableComponent(template.spriteId, template.renderOrder))
            .addComponent(new TimeValueComponent(template.baseSpeed))
            .addComponent(new HealthComponent(115))
            .addComponent(new IdentityComponent(template.name, template.actorId))
        ;
        BodyComponent bodyComp = BodyTemplateBuilder.build(template.bodyTemplate, template.maxHp);
        entity.addComponent(bodyComp);

        gameWorld.addEntity(entity);

        System.out.println("entity created and added to GameWorld\nBodyComp:");
        System.out.println(entity.getComponent(BodyComponent.class));
        return entity;
    }

    private static Entity buildFromTemplate(ActorTemplate template, GameWorld gameWorld) {
        return buildFromTemplate(template, gameWorld, new Point(0, 0));
    }

}
