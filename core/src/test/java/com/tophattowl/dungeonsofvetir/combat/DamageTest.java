package com.tophattowl.dungeonsofvetir.combat;

import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DamageTest {

    @Test
    void record_CreatesCorrectly() {
        Damage damage = new Damage(10, ElementType.PHYSICAL, DamageType.SLASHING);
        assertEquals(10, damage.amount());
        assertEquals(ElementType.PHYSICAL, damage.elementType());
        assertEquals(DamageType.SLASHING, damage.damageType());
    }

    @Test
    void record_AllDamageTypes() {
        for (DamageType type : DamageType.values()) {
            Damage damage = new Damage(10, ElementType.PHYSICAL, type);
            assertEquals(type, damage.damageType());
        }
    }

    @Test
    void record_AllElementTypes() {
        for (ElementType type : ElementType.values()) {
            Damage damage = new Damage(10, type, DamageType.SLASHING);
            assertEquals(type, damage.elementType());
        }
    }

    @Test
    void record_ZeroDamage() {
        Damage damage = new Damage(0, ElementType.PHYSICAL, DamageType.SLASHING);
        assertEquals(0, damage.amount());
    }

    @Test
    void record_NegativeDamage() {
        Damage damage = new Damage(-5, ElementType.PHYSICAL, DamageType.SLASHING);
        assertEquals(-5, damage.amount());
    }

    @Test
    void record_LargeDamage() {
        Damage damage = new Damage(10000, ElementType.PHYSICAL, DamageType.CRUSHING);
        assertEquals(10000, damage.amount());
    }

    @Test
    void allDamageTypes_Exist() {
        assertEquals(3, DamageType.values().length);
        assertNotNull(DamageType.SLASHING);
        assertNotNull(DamageType.PIERCING);
        assertNotNull(DamageType.CRUSHING);
    }

    @Test
    void allElementTypes_Exist() {
        assertEquals(4, ElementType.values().length);
        assertNotNull(ElementType.PHYSICAL);
        assertNotNull(ElementType.FIRE);
        assertNotNull(ElementType.LIGHTNING);
        assertNotNull(ElementType.POISON);
    }
}
