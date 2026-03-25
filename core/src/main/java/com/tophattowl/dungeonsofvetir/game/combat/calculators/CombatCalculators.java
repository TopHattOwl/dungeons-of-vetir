package com.tophattowl.dungeonsofvetir.game.combat.calculators;

import java.util.HashMap;
import java.util.Map;

public class CombatCalculators {
    private static final CombatCalculators INSTANCE = new CombatCalculators();

    private final Map<Class<? extends AttackCalculator>, AttackCalculator> calculators = new HashMap<>();

    private CombatCalculators() {}

    static {
        addCalculators();
    }

    public static <T extends AttackCalculator> T getCalculator(Class<T> clazz) {
        return (T) INSTANCE.calculators.get(clazz);
    }

    private static void addCalculators() {
        INSTANCE.calculators.put(MeleeAttackCalculator.class, new MeleeAttackCalculator());
    }
}
