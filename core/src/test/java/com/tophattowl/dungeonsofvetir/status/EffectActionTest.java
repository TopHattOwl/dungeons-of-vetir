package com.tophattowl.dungeonsofvetir.status;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.rng.SeedConfig;
import com.tophattowl.dungeonsofvetir.game.status.ApplyStatusAction;
import com.tophattowl.dungeonsofvetir.game.status.ChanceAction;
import com.tophattowl.dungeonsofvetir.game.status.CompositeAction;
import com.tophattowl.dungeonsofvetir.game.status.DamageAction;
import com.tophattowl.dungeonsofvetir.game.status.EffectContext;
import com.tophattowl.dungeonsofvetir.game.status.HealAction;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffect;
import com.tophattowl.dungeonsofvetir.game.status.StatusEffectId;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EffectActionTest {

    private final Random rng = new Random(1);
    private final EffectContext ctx = new EffectContext(null, rng);

    private static Entity entityWithHp(int hp) {
        Entity entity = new Entity();
        entity.addComponent(new HealthComponent(hp));
        return entity;
    }

    private static BodyPart arm() {
        return new BodyPart("arm", BodyPartType.ARM, BodyPartRole.LIMB,
            null, 100, 0, 0.1f, 1.0f, 1.0f);
    }

    private static StatusEffect boundTo(BodyPart part) {
        return new StatusEffect(StatusEffectId.BLEEDING, part, "test", 1, -1, new int[0]);
    }

    @Test
    void damage_subtractsHpAndBoundPartCondition() {
        Entity entity = entityWithHp(100);
        BodyPart arm = arm();
        entity.addComponent(new BodyComponent(List.of(arm)));

        new DamageAction(10, 5, ElementType.PHYSICAL, DamageType.SLASHING, true)
            .apply(ctx, entity, boundTo(arm));

        assertEquals(90, entity.getComponent(HealthComponent.class).hp);
        // condition takes 5 * the global 0.4 part scale
        assertEquals(98, arm.hp);
    }

    @Test
    void flatDamage_withoutBoundPart_onlyHitsHp() {
        Entity entity = entityWithHp(100);

        new DamageAction(7, 5, ElementType.FIRE, DamageType.CRUSHING, false)
            .apply(ctx, entity, null);

        assertEquals(93, entity.getComponent(HealthComponent.class).hp);
    }

    @Test
    void heal_restoresHp() {
        Entity entity = entityWithHp(100);
        entity.getComponent(HealthComponent.class).hp = 50;

        new HealAction(10, 0, null, null).apply(ctx, entity, null);

        assertEquals(60, entity.getComponent(HealthComponent.class).hp);
    }

    @Test
    void chance_runsAtFullProbability() {
        Entity entity = entityWithHp(100);

        new ChanceAction(1f, new DamageAction(5, 0, ElementType.PHYSICAL, DamageType.SLASHING, false))
            .apply(ctx, entity, null);

        assertEquals(95, entity.getComponent(HealthComponent.class).hp);
    }

    @Test
    void chance_skipsAtZeroProbability() {
        Entity entity = entityWithHp(100);

        new ChanceAction(0f, new DamageAction(5, 0, ElementType.PHYSICAL, DamageType.SLASHING, false))
            .apply(ctx, entity, null);

        assertEquals(100, entity.getComponent(HealthComponent.class).hp);
    }

    @Test
    void composite_runsAllActions() {
        Entity entity = entityWithHp(100);

        new CompositeAction(List.of(
            new DamageAction(3, 0, ElementType.PHYSICAL, DamageType.SLASHING, false),
            new DamageAction(4, 0, ElementType.PHYSICAL, DamageType.SLASHING, false)
        )).apply(ctx, entity, null);

        assertEquals(93, entity.getComponent(HealthComponent.class).hp);
    }

    @Test
    void applyStatus_appliesViaTheWorld() {
        GameWorld world = new GameWorld(SeedConfig.custom(3));
        Entity entity = new Entity();
        entity.addComponent(new PositionComponent(5, 1));
        entity.addComponent(new com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent());
        world.addEntity(entity);

        EffectContext worldCtx = new EffectContext(world, rng);
        new ApplyStatusAction(StatusEffectId.POISONED, 1, 4).apply(worldCtx, entity, null);

        assertTrue(entity.getComponent(com.tophattowl.dungeonsofvetir.game.status.StatusEffectComponent.class)
            .has(StatusEffectId.POISONED));
    }
}
