package com.tophattowl.dungeonsofvetir.game.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;

public class CombatCalculator {

    public static AttackResult getAttackResult(Entity attacker, Entity target) {
        return null;
    }

    public record AttackResult(int healthDamage, int bodyPartDamage,
                               boolean blocked, boolean missed, boolean countered) {

    }
}
