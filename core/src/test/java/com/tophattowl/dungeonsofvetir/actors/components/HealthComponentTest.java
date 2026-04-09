package com.tophattowl.dungeonsofvetir.actors.components;

import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.HealthComponent.HealthStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HealthComponentTest {

    @Test
    void constructor_SetsHpToMax() {
        HealthComponent health = new HealthComponent(100);
        assertEquals(100, health.maxHp);
        assertEquals(100, health.hp);
    }

    @Test
    void constructor_SetsStatusHealthy() {
        HealthComponent health = new HealthComponent(100);
        assertEquals(HealthStatus.HEALTHY, health.status);
    }

    @Test
    void constructor_ZeroMaxHp() {
        HealthComponent health = new HealthComponent(0);
        assertEquals(0, health.maxHp);
        assertEquals(0, health.hp);
    }

    @Test
    void takeDamage_ReducesHp() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(30);
        assertEquals(70, health.hp);
    }

    @Test
    void takeDamage_FloorsAtZero() {
        HealthComponent health = new HealthComponent(10);
        health.takeDamage(50);
        assertEquals(0, health.hp);
    }

    @Test
    void takeDamage_ReturnsFalse_WhenAlive() {
        HealthComponent health = new HealthComponent(100);
        boolean result = health.takeDamage(30);
        assertFalse(result);
    }

    @Test
    void takeDamage_ReturnsTrue_WhenDead() {
        HealthComponent health = new HealthComponent(10);
        boolean result = health.takeDamage(50);
        assertTrue(result);
    }

    @Test
    void takeDamage_UpdatesStatus_Healthy() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(10);
        assertEquals(HealthStatus.HEALTHY, health.status);
    }

    @Test
    void takeDamage_UpdatesStatus_Fine() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(40);
        assertEquals(HealthStatus.FINE, health.status);
    }

    @Test
    void takeDamage_UpdatesStatus_Hurt() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(60);
        assertEquals(HealthStatus.HURT, health.status);
    }

    @Test
    void takeDamage_UpdatesStatus_Injured() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(75);
        assertEquals(HealthStatus.INJURED, health.status);
    }

    @Test
    void takeDamage_UpdatesStatus_Critical() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(90);
        assertEquals(HealthStatus.CRITICAL, health.status);
    }

    @Test
    void status_Healthy_Above70Percent() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(29);
        assertEquals(71, health.hp);
        assertEquals(HealthStatus.HEALTHY, health.status);
    }

    @Test
    void status_Fine_At70Percent() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(30);
        assertEquals(70, health.hp);
        assertEquals(HealthStatus.FINE, health.status);
    }

    @Test
    void status_Fine_Below70Above50() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(40);
        assertEquals(60, health.hp);
        assertEquals(HealthStatus.FINE, health.status);
    }

    @Test
    void status_Hurt_At50Percent() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(50);
        assertEquals(50, health.hp);
        assertEquals(HealthStatus.HURT, health.status);
    }

    @Test
    void status_Hurt_Below50Above35() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(60);
        assertEquals(40, health.hp);
        assertEquals(HealthStatus.HURT, health.status);
    }

    @Test
    void status_Injured_At35Percent() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(65);
        assertEquals(35, health.hp);
        assertEquals(HealthStatus.INJURED, health.status);
    }

    @Test
    void status_Injured_Below35Above20() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(75);
        assertEquals(25, health.hp);
        assertEquals(HealthStatus.INJURED, health.status);
    }

    @Test
    void status_Critical_At20Percent() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(80);
        assertEquals(20, health.hp);
        assertEquals(HealthStatus.CRITICAL, health.status);
    }

    @Test
    void status_Critical_Below20() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(85);
        assertEquals(15, health.hp);
        assertEquals(HealthStatus.CRITICAL, health.status);
    }

    @Test
    void status_Dead_At0Percent() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(100);
        assertEquals(0, health.hp);
        assertEquals(HealthStatus.CRITICAL, health.status);
    }

    @Test
    void multipleDamageApplications() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(10);
        health.takeDamage(10);
        health.takeDamage(10);
        assertEquals(70, health.hp);
        assertEquals(HealthStatus.FINE, health.status);
    }

    @Test
    void healthStatus_Thresholds() {
        assertEquals(0.9f, HealthStatus.HEALTHY.threshold);
        assertEquals(0.7f, HealthStatus.FINE.threshold);
        assertEquals(0.5f, HealthStatus.HURT.threshold);
        assertEquals(0.35f, HealthStatus.INJURED.threshold);
        assertEquals(0.2f, HealthStatus.CRITICAL.threshold);
    }
}
