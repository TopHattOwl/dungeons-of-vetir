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
import com.tophattowl.dungeonsofvetir.game.stats.StatAggregator;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffectComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Point;

public class EntityFactory {

    public static Entity createEntity(ActorId actorId, GameWorld gameWorld, Point spawnPos) {
        ActorSpec spec = ActorRegistry.get(actorId);
        return buildFromSpec(spec, gameWorld, spawnPos);
    }

    public static Entity makePlayer(StatAggregator stats, Level level, Point spawnPoint) {
        Entity player = new Entity();
        player.addComponent(new PositionComponent(spawnPoint))
            .addComponent(new RenderableComponent("player_knight", 10))
            .addComponent(new TimeValueComponent())
            .addComponent(new FovComponent(10, level.getWidth(), level.getHeight()))
            .addComponent(new PlayerComponent())
            .addComponent(new IdentityComponent("player", ActorId.PLAYER, Faction.HUNTER))
            .addComponent(new AttributesComponent(10, 10, 10, 10, 10))
            .addComponent(new EquipmentComponent())
            .addComponent(new StatusEffectComponent())
        ;

        stats.recompute(player);

        int maxHp = player.getComponent(HealthComponent.class).maxHp;
        BodyComponent bodyComp = BodyComponentBuilder.build(BodyTemplate.HUMANOID, maxHp, null);
        player.addComponent(bodyComp);

        player.getComponent(EquipmentComponent.class).initSlots(bodyComp);

        stats.recompute(player);

        return player;
    }

    private static Entity buildFromSpec(ActorSpec spec, GameWorld gameWorld, Point spawnPos) {
        Entity entity = new Entity();

        // base comps (all actors have)
        PositionComponent posComp = new PositionComponent(spawnPos);
        IdentityComponent identityComp = new IdentityComponent(spec.name(), spec.actorId(), spec.faction());
        entity.addComponent(posComp)
            .addComponent(identityComp);

        // before body specs
        for (ActorComponentSpec<?> baseSpec : spec.baseSpecs()) {
            entity.addComponent(baseSpec.build(entity, gameWorld));
        }
        entity.addComponent(new StatusEffectComponent());

        // derive stats from attributes before the body is built (creates HealthComponent)
        gameWorld.stats().recompute(entity);

        // body
        BodySpec bodySpec = spec.bodySpec();
        if (!entity.hasComponent(HealthComponent.class)) {
            throw new IllegalStateException("Entity should have Health component, ActorId: " + spec.actorId());
        }
        int maxHp = entity.getComponent(HealthComponent.class).maxHp;
        BodyComponent bodyComp = BodyComponentBuilder.build(bodySpec.bodyTemplate(), maxHp, bodySpec.naturalProts());
        entity.addComponent(bodyComp);

        // post body
        for (ActorComponentSpec<?> postBodySpec : spec.postBodySpecs()) {
            entity.addComponent(postBodySpec.build(entity, gameWorld));
        }

        // rebuild with the body present (protection map, part max HP)
        gameWorld.stats().recompute(entity);

        // normalize time value
        TimeValueComponent timeComp = entity.getComponent(TimeValueComponent.class);
        timeComp.addTime(gameWorld.timeTurnManager.getTurnEventTime());

        gameWorld.addEntity(entity);

        return entity;
    }
}
