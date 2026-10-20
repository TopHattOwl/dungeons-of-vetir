package com.tophattowl.dungeonsofvetir.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.meta.MetaState;
import com.tophattowl.dungeonsofvetir.game.rng.SeedConfig;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.stats.ModifierOperation;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffectComponent;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffectId;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.WorldContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StatusEffectTest {

    private static GameWorld newWorld() {
        return new GameWorld(SeedConfig.custom(11));
    }

    private static Entity entity(GameWorld world, int x) {
        Entity entity = new Entity();
        entity.addComponent(new PositionComponent(x, 1));
        entity.addComponent(new com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent());
        world.addEntity(entity);
        return entity;
    }

    private static BodyPart part() {
        return new BodyPart("arm", BodyPartType.ARM, BodyPartRole.LIMB,
            null, 100, 0, 0.1f, 1.0f, 1.0f);
    }

    @Test
    void appliedEffect_deniesCapabilities() {
        GameWorld world = newWorld();
        Entity entity = entity(world, 5);

        world.statusEffects().apply(entity, StatusEffectId.STUNNED, "test", 1, 3);

        assertFalse(world.constraints().can(entity, Capability.MOVE));
        assertFalse(world.constraints().can(entity, Capability.ATTACK));
        assertTrue(world.constraints().can(entity, Capability.REST));
    }

    @Test
    void timedEffect_expiresAndRestoresCapabilities() {
        GameWorld world = newWorld();
        WorldContext context = new WorldContext(world, new MetaState());
        Entity entity = entity(world, 6);

        world.statusEffects().apply(entity, StatusEffectId.STUNNED, "test", 1, 2);
        assertFalse(world.constraints().can(entity, Capability.MOVE));

        context.processTurn();
        assertFalse(world.constraints().can(entity, Capability.MOVE));
        context.processTurn();
        assertTrue(world.constraints().can(entity, Capability.MOVE), "should expire after 2 turns");
    }

    @Test
    void intensityStacking_accumulatesStacks() {
        GameWorld world = newWorld();
        Entity entity = entity(world, 7);
        BodyPart arm = part();

        world.statusEffects().apply(entity, StatusEffectId.BLEEDING, arm, "test", 1, 5);
        world.statusEffects().apply(entity, StatusEffectId.BLEEDING, arm, "test", 1, 5);

        StatusEffectComponent component = entity.getComponent(StatusEffectComponent.class);
        assertEquals(2, component.find(StatusEffectId.BLEEDING, arm).stacks());
    }

    @Test
    void refresh_doesNotStack() {
        GameWorld world = newWorld();
        Entity entity = entity(world, 8);

        world.statusEffects().apply(entity, StatusEffectId.STUNNED, "test", 1, 3);
        world.statusEffects().apply(entity, StatusEffectId.STUNNED, "test", 1, 3);

        StatusEffectComponent component = entity.getComponent(StatusEffectComponent.class);
        assertEquals(1, component.find(StatusEffectId.STUNNED, null).stacks());
    }

    @Test
    void modifiers_areContributedToAggregation() {
        StatusEffectComponent component = new StatusEffectComponent();
        Entity entity = new Entity();
        entity.addComponent(component);

        GameWorld world = newGameWorldWith(entity, StatusEffectId.STUNNED);

        List<Modifier> modifiers = new ArrayList<>();
        component.collectModifiers(modifiers);

        assertEquals(1, modifiers.size());
        assertEquals(ModifierOperation.PERCENT, modifiers.get(0).operation());
        assertEquals(-0.5f, modifiers.get(0).value(), 1e-4f);
    }

    private static GameWorld newGameWorldWith(Entity entity, StatusEffectId id) {
        GameWorld world = newWorld();
        entity.addComponent(new PositionComponent(9, 1));
        entity.addComponent(new com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent());
        world.addEntity(entity);
        world.statusEffects().apply(entity, id, "test", 1, 3);
        return world;
    }

    @Test
    void remove_clearsTheEffect() {
        GameWorld world = newWorld();
        Entity entity = entity(world, 10);

        world.statusEffects().apply(entity, StatusEffectId.STUNNED, "test", 1, 5);
        world.statusEffects().remove(entity, StatusEffectId.STUNNED, "cured");

        assertTrue(world.constraints().can(entity, Capability.MOVE));
        assertFalse(entity.getComponent(StatusEffectComponent.class).has(StatusEffectId.STUNNED));
    }
}
