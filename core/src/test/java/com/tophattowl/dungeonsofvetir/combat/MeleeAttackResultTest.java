package com.tophattowl.dungeonsofvetir.combat;

import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartRole;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPartType;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MeleeAttackResultTest {

    @Test
    void constructor_InitializesFields() {
        MeleeAttackResult result = new MeleeAttackResult();
        assertFalse(result.isMissed());
        assertFalse(result.isBlocked());
        assertFalse(result.isCountered());
        assertNull(result.getBodyPart());
        assertTrue(result.getDamages().isEmpty());
    }

    @Test
    void addDamage_AddsToList() {
        MeleeAttackResult result = new MeleeAttackResult();
        Damage damage = new Damage(10, ElementType.PHYSICAL, DamageType.SLASHING);
        result.addDamage(damage);

        List<Damage> damages = result.getDamages();
        assertEquals(1, damages.size());
        assertEquals(damage, damages.get(0));
    }

    @Test
    void addDamage_MultipleDamages() {
        MeleeAttackResult result = new MeleeAttackResult();
        result.addDamage(new Damage(10, ElementType.PHYSICAL, DamageType.SLASHING));
        result.addDamage(new Damage(5, ElementType.FIRE, DamageType.SLASHING));
        result.addDamage(new Damage(3, ElementType.POISON, DamageType.PIERCING));

        assertEquals(3, result.getDamages().size());
    }

    @Test
    void setBodyPart_SetsField() {
        MeleeAttackResult result = new MeleeAttackResult();
        BodyPart part = new BodyPart("head", BodyPartType.HEAD, BodyPartRole.VITAL,
            null, 50, 0, 0.1f, 0.1f, 1.2f);

        result.setBodyPart(part);

        assertSame(part, result.getBodyPart());
    }

    @Test
    void missed_SetsFlag() {
        MeleeAttackResult result = new MeleeAttackResult();
        result.missed();
        assertTrue(result.isMissed());
    }

    @Test
    void blocked_SetsFlag() {
        MeleeAttackResult result = new MeleeAttackResult();
        result.blocked();
        assertTrue(result.isBlocked());
    }

    @Test
    void countered_SetsFlag() {
        MeleeAttackResult result = new MeleeAttackResult();
        result.countered();
        assertTrue(result.isCountered());
    }

    @Test
    void flags_CanBeCombined() {
        MeleeAttackResult result = new MeleeAttackResult();
        result.missed();
        result.blocked();

        assertTrue(result.isMissed());
        assertTrue(result.isBlocked());
    }

    @Test
    void countered_WithoutMissedOrBlocked() {
        MeleeAttackResult result = new MeleeAttackResult();
        result.countered();

        assertFalse(result.isMissed());
        assertFalse(result.isBlocked());
        assertTrue(result.isCountered());
    }

    @Test
    void getDamages_ReturnsAllAdded() {
        MeleeAttackResult result = new MeleeAttackResult();
        Damage d1 = new Damage(10, ElementType.PHYSICAL, DamageType.SLASHING);
        Damage d2 = new Damage(5, ElementType.FIRE, DamageType.CRUSHING);
        result.addDamage(d1);
        result.addDamage(d2);

        List<Damage> damages = result.getDamages();
        assertEquals(2, damages.size());
        assertTrue(damages.contains(d1));
        assertTrue(damages.contains(d2));
    }
}
