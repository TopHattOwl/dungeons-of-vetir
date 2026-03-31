package com.tophattowl.dungeonsofvetir.game.combat.calculators;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.combat.context.AttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.items.EquipmentSlotType;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.*;

public class MeleeAttackCalculator implements AttackCalculator<MeleeAttackResult> {

    private List<EquipmentComponent.EquipmentSlot> wielding;
    private Item mainHandItem;
    private Item offHandItem;

    @Override
    public List<MeleeAttackResult> calculate(AttackContext context, GameWorld gameWorld) {
        MeleeAttackResult result = new MeleeAttackResult();

        Entity attacker = context.getAttacker();
        Entity target = context.getTarget();
        OffensiveStatsComponent attackerOffense = attacker.getComponent(OffensiveStatsComponent.class);
        EquipmentComponent attackerEquipment = attacker.getComponent(EquipmentComponent.class);

        mainHandItem = attackerEquipment.getMainHandSlot().item;
        offHandItem = attackerEquipment.getOffHandSlot().item;

        // get main hand item, that always attacks
        // get offhands and separate them based on ItemType(=MELEE_WEAPON -> offHandWeapons)
        // get bonuses that need to be applied from non weapon offhands,
        // then attack with each weapon (applying hand efficiency correctly, mainHandEfficiency for main hand...)
            // making a separate MeleeAttackResult for each attack

        // special case:
            // no equipment comp/no equipments -> base damage

        // need to do:
            // give all items a melee weapon comp

        // roll a new random bodypart for each weapon

        // attacks flow:
            // accuracy check
            // block check
            // attack
            // counter


        HealthComponent targetHealthComp = target.getComponent(HealthComponent.class);
        DefensiveStatsComponent targetDefence = target.getComponent(DefensiveStatsComponent.class);
        BodyComponent targetBodyComp = target.getComponent(BodyComponent.class);
        BodyPart targetedBodyPart = targetBodyComp.getRandomBodyPart();

        result.setBodyPart(targetedBodyPart);



        return List.of(result);
    }

    @Override
    public AttackType getType() {
        return AttackType.MELEE;
    }
}
