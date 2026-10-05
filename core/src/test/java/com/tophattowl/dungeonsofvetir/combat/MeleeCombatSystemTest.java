package com.tophattowl.dungeonsofvetir.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.MeleeAttackAction;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyComponentBuilder;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyTemplate;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.DefensiveStatsComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.EquipmentComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.OffensiveStatsComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.TimeValueComponent;
import com.tophattowl.dungeonsofvetir.game.combat.combat_systems.MeleeCombatSystem;
import com.tophattowl.dungeonsofvetir.game.event.EventBus;
import com.tophattowl.dungeonsofvetir.game.event.events.combat.EntityKilledEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.combat.MeleeAttackCounteredEvent;
import com.tophattowl.dungeonsofvetir.game.rng.SeedConfig;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class MeleeCombatSystemTest {

    private static GameWorld newWorld() {
        return new GameWorld(SeedConfig.custom(7));
    }

    private static BodyPart newPart(int maxHp, float damageMultiplier) {
        return new BodyPart("torso", BodyPartType.TORSO, BodyPartRole.VITAL,
            null, maxHp, 0, 1.0f, 1.0f, damageMultiplier);
    }

    private static Entity spawnAttacker(GameWorld world, int baseDamage, int x, int y) {
        Entity attacker = new Entity();
        BodyComponent body = BodyComponentBuilder.build(BodyTemplate.HUMANOID, 100, null);
        EquipmentComponent equipment = new EquipmentComponent();
        equipment.initSlots(body);

        attacker.addComponent(body)
            .addComponent(new OffensiveStatsComponent(baseDamage, 1f, 1f, 1f, 100))
            .addComponent(equipment)
            .addComponent(new PositionComponent(x, y))
            .addComponent(new TimeValueComponent());

        world.addEntity(attacker);
        return attacker;
    }

    private static Entity spawnTarget(GameWorld world, BodyPart part, int hp,
                                      int evasion, float counter, float block, int x, int y) {
        Entity target = new Entity();
        BodyComponent body = new BodyComponent(List.of(part));

        target.addComponent(body)
            .addComponent(new HealthComponent(hp))
            .addComponent(new DefensiveStatsComponent(evasion, counter, block, body))
            .addComponent(new PositionComponent(x, y))
            .addComponent(new TimeValueComponent());

        world.addEntity(target);
        return target;
    }

    private static void attack(GameWorld world, Entity attacker, Entity target) {
        MeleeCombatSystem.executeMeleeAttack(new MeleeAttackAction(attacker, target), world);
    }

    @Test
    void hit_ReducesHpAndBodyPartCondition() {
        GameWorld world = newWorld();
        BodyPart part = newPart(100, 1.0f);
        Entity target = spawnTarget(world, part, 1000, 0, 0f, 0f, 2, 1);
        Entity attacker = spawnAttacker(world, 10, 1, 1);

        attack(world, attacker, target);

        assertEquals(990, target.getComponent(HealthComponent.class).hp);
        // condition takes (10 * damageMultiplier) then the global 0.4 condition scale
        assertEquals(96, part.hp);
        assertTrue(world.getAllEntities().contains(target));
    }

    @Test
    void lethalHpDamage_KillsAndRemovesTarget() {
        GameWorld world = newWorld();
        Entity target = spawnTarget(world, newPart(1000, 1.0f), 5, 0, 0f, 0f, 2, 1);
        Entity attacker = spawnAttacker(world, 10, 1, 1);

        AtomicBoolean killed = new AtomicBoolean(false);
        EventBus.ListenerHandle<EntityKilledEvent> handle =
            EventBus.on(EntityKilledEvent.class, e -> killed.set(true));

        try {
            attack(world, attacker, target);
            assertFalse(world.getAllEntities().contains(target));
            assertTrue(killed.get());
        } finally {
            EventBus.off(handle);
        }
    }

    @Test
    void destroyedVitalPart_KillsTargetEvenWithHpRemaining() {
        GameWorld world = newWorld();
        BodyPart vital = newPart(1, 1.0f);
        Entity target = spawnTarget(world, vital, 100000, 0, 0f, 0f, 2, 1);
        Entity attacker = spawnAttacker(world, 10, 1, 1);

        attack(world, attacker, target);

        assertTrue(vital.isDestroyed());
        assertFalse(world.getAllEntities().contains(target), "vital part loss should kill");
    }

    @Test
    void counter_AppliesWhenTargetSurvives() {
        GameWorld world = newWorld();
        Entity target = spawnTarget(world, newPart(1000, 1.0f), 1000, 0, 1f, 0f, 2, 1);
        Entity attacker = spawnAttacker(world, 10, 1, 1);

        AtomicBoolean countered = new AtomicBoolean(false);
        EventBus.ListenerHandle<MeleeAttackCounteredEvent> handle =
            EventBus.on(MeleeAttackCounteredEvent.class, e -> countered.set(true));

        try {
            attack(world, attacker, target);
            assertTrue(countered.get());
        } finally {
            EventBus.off(handle);
        }
    }

    @Test
    void counter_DoesNotApplyWhenTargetDies() {
        GameWorld world = newWorld();
        Entity target = spawnTarget(world, newPart(1000, 1.0f), 5, 0, 1f, 0f, 2, 1);
        Entity attacker = spawnAttacker(world, 10, 1, 1);

        AtomicBoolean countered = new AtomicBoolean(false);
        EventBus.ListenerHandle<MeleeAttackCounteredEvent> handle =
            EventBus.on(MeleeAttackCounteredEvent.class, e -> countered.set(true));

        try {
            attack(world, attacker, target);
            assertFalse(countered.get(), "the dead should not counter");
        } finally {
            EventBus.off(handle);
        }
    }
}
