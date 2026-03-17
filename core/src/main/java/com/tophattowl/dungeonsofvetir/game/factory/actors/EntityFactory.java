package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
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
            .addComponent(new IdentityComponent("player", ActorId.PLAYER, Faction.HUNTER))
            .addComponent(new HealthComponent(213))
            .addComponent(new OffensiveStatsComponent(
                30, 1.0f,
                1.0f, 1.0f,
                1.0f))
        ;

        int maxHp = player.getComponent(HealthComponent.class).getMaxHp();
        BodyComponent bodyComp = BodyComponentBuilder.build(BodyTemplate.HUMANOID, maxHp);
        player.addComponent(bodyComp);

        return player;
    }

    private static Entity buildFromTemplate(ActorTemplate template, GameWorld gameWorld, Point spawnPos) {
        Entity entity = new Entity();

        entity.addComponent(new PositionComponent(spawnPos))
            .addComponent(new RenderableComponent(template.spriteId, template.renderOrder))
            .addComponent(new TimeValueComponent(template.baseSpeed))
            .addComponent(new HealthComponent(template.maxHp))
            .addComponent(new IdentityComponent(template.name, template.actorId, template.faction))
            .addComponent(new FovComponent(template.visionRange, Level.WIDTH, Level.HEIGHT))
            .addComponent(new OffensiveStatsComponent(template.baseDamage, template.weaponDamageModifier,
                template.mainHandEfficiency, template.offHandEfficiencyModifier, template.accuracy))
        ;

        BodyComponent bodyComp = BodyComponentBuilder.build(template.bodyTemplate, template.maxHp);
        entity.addComponent(bodyComp);

        gameWorld.addEntity(entity);

        DebugLogger.log(DebugLogger.Category.FACTORY, "EntityFactory",
            "Entity created:\n" + entity + "\nBodyComponent:\n" + bodyComp
        );
        return entity;
    }

    private static Entity buildFromTemplate(ActorTemplate template, GameWorld gameWorld) {
        return buildFromTemplate(template, gameWorld, new Point(0, 0));
    }

}
