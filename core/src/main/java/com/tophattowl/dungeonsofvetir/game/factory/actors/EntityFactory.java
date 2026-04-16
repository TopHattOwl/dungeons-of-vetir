package com.tophattowl.dungeonsofvetir.game.factory.actors;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;
import com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs.ActorComponentSpec;
import com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs.BodySpec;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public class EntityFactory {

    public static Entity createEntity(ActorId actorId, GameWorld gameWorld, Point spawnPos) {
        ActorSpec spec = ActorRegistry.get(actorId);
        return buildFromSpec(spec, gameWorld, spawnPos);
    }

    public static Entity makePlayer(Point spawnPoint) {
        Entity player = new Entity();
        player.addComponent(new PositionComponent(spawnPoint))
            .addComponent(new RenderableComponent("player", 10))
            .addComponent(new TimeValueComponent())
            .addComponent(new FovComponent(10, Level.WIDTH, Level.HEIGHT))
            .addComponent(new PlayerComponent())
            .addComponent(new IdentityComponent("player", ActorId.PLAYER, Faction.HUNTER))
            .addComponent(new HealthComponent(350))
            .addComponent(new OffensiveStatsComponent(
                30, 1.0f,
                1.0f, 1.0f,
                60))
            .addComponent(new EquipmentComponent())
        ;

        int maxHp = player.getComponent(HealthComponent.class).maxHp;
        BodyComponent bodyComp = BodyComponentBuilder.build(BodyTemplate.HUMANOID, maxHp, null);
        player.addComponent(bodyComp);

        DefensiveStatsComponent defenseComp = new DefensiveStatsComponent(
            25, 0.1f, 0.1f, bodyComp);

        player.addComponent(defenseComp);

        player.getComponent(EquipmentComponent.class).initSlots(bodyComp);

        return player;
    }

    private static Entity buildFromSpec(ActorSpec spec, GameWorld gameWorld, Point spawnPos) {
        Entity entity = new Entity();

        PositionComponent posComp = new PositionComponent(spawnPos);
        IdentityComponent identityComp = new IdentityComponent(spec.name(), spec.actorId(), spec.faction());
        entity.addComponent(posComp)
            .addComponent(identityComp);

        for (ActorComponentSpec<?> baseSpec : spec.baseSpecs()) {
            entity.addComponent(baseSpec.build(entity));
        }

        BodySpec bodySpec = spec.bodySpec();
        int maxHp = entity.getComponent(HealthComponent.class).maxHp;
        BodyComponent bodyComp = BodyComponentBuilder.build(bodySpec.bodyTemplate(), maxHp, bodySpec.naturalProts());
        entity.addComponent(bodyComp);

        for (ActorComponentSpec<?> postBodySpec : spec.postBodySpecs()) {
            entity.addComponent(postBodySpec.build(entity));
        }

        // normalize time value
        TimeValueComponent timeComp = entity.getComponent(TimeValueComponent.class);
        timeComp.addTime(gameWorld.timeTurnManager.getTurnEventTime());

        gameWorld.addEntity(entity);

        return entity;
    }
}
