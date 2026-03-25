package com.tophattowl.dungeonsofvetir.game.combat.calculators;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.combat.context.AttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.List;

public class MeleeAttackCalculator implements AttackCalculator {
    @Override
    public MeleeAttackResult calculate(AttackContext context, GameWorld gameWorld) {
        MeleeAttackResult result = new MeleeAttackResult();

        Entity attacker = context.getAttacker();
        Entity target = context.getTarget();
        OffensiveStatsComponent attackerOffense = attacker.getComponent(OffensiveStatsComponent.class);
        EquipmentComponent attackerEquipment = attacker.getComponent(EquipmentComponent.class);
        List<EquipmentComponent.EquipmentSlot> wielding = attackerEquipment.getByEquipmentSlotType(
            EquipmentSlotType.HAND_SLOT);

        // check two handed /dual wield/ one handed / or no item

        HealthComponent targetHealthComp = target.getComponent(HealthComponent.class);
        DefensiveStatsComponent targetDefence = target.getComponent(DefensiveStatsComponent.class);
        BodyComponent targetBodyComp = target.getComponent(BodyComponent.class);
        BodyPart targetedBodyPart = targetBodyComp.getRandomBodyPart();

        result.setBodyPart(targetedBodyPart);



        // accuracy check

        // block check

        // counter



        return result;
    }

    @Override
    public AttackType getType() {
        return AttackType.MELEE;
    }
}
