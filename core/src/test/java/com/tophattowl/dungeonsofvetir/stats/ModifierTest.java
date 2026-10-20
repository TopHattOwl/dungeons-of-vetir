package com.tophattowl.dungeonsofvetir.stats;

import com.tophattowl.dungeonsofvetir.game.stats.Attribute;
import com.tophattowl.dungeonsofvetir.game.stats.CombatStat;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.stats.ModifierOperation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModifierTest {

    @Test
    void factoriesSetOperation() {
        assertEquals(ModifierOperation.FLAT, Modifier.flat(Attribute.THEW, 2f).operation());
        assertEquals(ModifierOperation.PERCENT, Modifier.percent(CombatStat.ACCURACY, 0.1f).operation());
        assertEquals(ModifierOperation.MULTIPLY, Modifier.multiply(CombatStat.MELEE_DAMAGE, 2f).operation());
    }

    @Test
    void targetCanBeAttributeOrStat() {
        Modifier onAttribute = Modifier.flat(Attribute.WIND, 1f);
        Modifier onStat = Modifier.flat(CombatStat.DODGE_CHANCE, 0.05f);

        assertEquals(Attribute.WIND, onAttribute.target());
        assertEquals(CombatStat.DODGE_CHANCE, onStat.target());
    }

    @Test
    void modifiableByCollector() {
        List<Modifier> out = new ArrayList<>();
        out.add(Modifier.flat(Attribute.FLESH, 3f));
        out.add(Modifier.percent(CombatStat.MAX_HP, 0.25f));

        assertEquals(2, out.size());
    }
}
