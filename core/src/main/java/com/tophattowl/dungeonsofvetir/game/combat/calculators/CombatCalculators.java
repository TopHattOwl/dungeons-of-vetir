package com.tophattowl.dungeonsofvetir.game.combat.calculators;

public class CombatCalculators {
    private static final CombatCalculators INSTANCE = new CombatCalculators();

    private final MeleeAttackCalculator meleeCalc = new MeleeAttackCalculator();


    private CombatCalculators() {}

    public static MeleeAttackCalculator getMeleeCalculator() {
        return INSTANCE.meleeCalc;
    }
}
