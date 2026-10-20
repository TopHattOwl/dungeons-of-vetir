package com.tophattowl.dungeonsofvetir.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.AttributesComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.meta.MetaState;
import com.tophattowl.dungeonsofvetir.game.rng.SeedConfig;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffectComponent;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffectId;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.WorldContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegenTest {

    private static Entity regenEntity(GameWorld world, int x, int hp) {
        Entity entity = new Entity();
        entity.addComponent(new PositionComponent(x, 1));
        entity.addComponent(new com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent());
        world.addEntity(entity);
        entity.addComponent(new AttributesComponent(10, 10, 10, 10, 10));
        entity.addComponent(new HealthComponent(350));
        world.stats().recompute(entity);
        entity.getComponent(HealthComponent.class).hp = hp;
        return entity;
    }

    @Test
    void damagedEntity_regeneratesOverTurns() {
        GameWorld world = new GameWorld(SeedConfig.custom(31));
        WorldContext context = new WorldContext(world, new MetaState());
        Entity entity = regenEntity(world, 20, 200);

        for (int i = 0; i < 8; i++) context.processTurn();

        assertTrue(entity.getComponent(HealthComponent.class).hp > 200, "should have regenerated");
        assertTrue(entity.getComponent(StatusEffectComponent.class).has(StatusEffectId.REGENERATING));
    }

    @Test
    void recentlyWounded_suppressesRegeneration() {
        GameWorld world = new GameWorld(SeedConfig.custom(32));
        WorldContext context = new WorldContext(world, new MetaState());
        Entity entity = regenEntity(world, 21, 200);

        world.statusEffects().apply(entity, StatusEffectId.RECENTLY_WOUNDED, "test", 1, 50);

        for (int i = 0; i < 8; i++) context.processTurn();

        assertEquals(200, entity.getComponent(HealthComponent.class).hp);
        assertFalse(entity.getComponent(StatusEffectComponent.class).has(StatusEffectId.REGENERATING));
    }

    @Test
    void fullHealth_doesNotRegenerate() {
        GameWorld world = new GameWorld(SeedConfig.custom(33));
        WorldContext context = new WorldContext(world, new MetaState());
        Entity entity = regenEntity(world, 22, 350);

        context.processTurn();

        StatusEffectComponent component = entity.getComponent(StatusEffectComponent.class);
        assertTrue(component == null || !component.has(StatusEffectId.REGENERATING));
        assertEquals(350, entity.getComponent(HealthComponent.class).hp);
    }
}
