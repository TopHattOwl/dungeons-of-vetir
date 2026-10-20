package com.tophattowl.dungeonsofvetir.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.meta.MetaState;
import com.tophattowl.dungeonsofvetir.game.rng.SeedConfig;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffectComponent;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffectId;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.WorldContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateRuleTest {

    private static Entity entity(GameWorld world, int x) {
        Entity entity = new Entity();
        entity.addComponent(new PositionComponent(x, 1));
        entity.addComponent(new com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent());
        world.addEntity(entity);
        return entity;
    }

    private static StatusEffectComponent effects(Entity entity) {
        return entity.getComponent(StatusEffectComponent.class);
    }

    @Test
    void wounded_appliedBelowThreshold_andRemovedAbove() {
        GameWorld world = new GameWorld(SeedConfig.custom(21));
        WorldContext context = new WorldContext(world, new MetaState());
        Entity entity = entity(world, 5);
        entity.addComponent(new HealthComponent(100));
        entity.getComponent(HealthComponent.class).hp = 30;

        context.processTurn();
        assertTrue(effects(entity).has(StatusEffectId.WOUNDED), "should be wounded at 30%");

        entity.getComponent(HealthComponent.class).hp = 100;
        context.processTurn();
        assertFalse(effects(entity).has(StatusEffectId.WOUNDED), "should not be wounded at full HP");
    }

    @Test
    void concussed_whenHeadDestroyed() {
        GameWorld world = new GameWorld(SeedConfig.custom(22));
        WorldContext context = new WorldContext(world, new MetaState());
        Entity entity = entity(world, 6);
        entity.addComponent(new HealthComponent(500));

        BodyPart head = new BodyPart("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            null, 100, 0, 0.2f, 1.0f, 1.0f);
        entity.addComponent(new BodyComponent(List.of(head)));
        head.takeDamage(1000);

        context.processTurn();
        assertTrue(effects(entity).has(StatusEffectId.CONCUSSED));
    }

    @Test
    void crippled_whenLimbBadlyDamaged() {
        GameWorld world = new GameWorld(SeedConfig.custom(23));
        WorldContext context = new WorldContext(world, new MetaState());
        Entity entity = entity(world, 7);
        entity.addComponent(new HealthComponent(500));

        BodyPart arm = new BodyPart("arm", BodyPartType.ARM, BodyPartRole.LIMB,
            null, 100, 0, 0.1f, 1.0f, 1.0f);
        entity.addComponent(new BodyComponent(List.of(arm)));
        arm.takeDamage(200); // 100 - 200*0.4 -> 20 (ratio 0.2)

        context.processTurn();
        assertTrue(effects(entity).has(StatusEffectId.CRIPPLED));
    }

    @Test
    void healthyEntity_hasNoStateEffects() {
        GameWorld world = new GameWorld(SeedConfig.custom(24));
        WorldContext context = new WorldContext(world, new MetaState());
        Entity entity = entity(world, 8);
        entity.addComponent(new HealthComponent(100));

        context.processTurn();
        StatusEffectComponent component = entity.getComponent(StatusEffectComponent.class);
        assertTrue(component == null || !component.has(StatusEffectId.WOUNDED));
        assertTrue(component == null || !component.has(StatusEffectId.CRIPPLED));
        assertTrue(component == null || !component.has(StatusEffectId.CONCUSSED));
    }
}
