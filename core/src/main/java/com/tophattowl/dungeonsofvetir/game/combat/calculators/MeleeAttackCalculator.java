package com.tophattowl.dungeonsofvetir.game.combat.calculators;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.*;
import com.tophattowl.dungeonsofvetir.game.actors.monsters.NaturalWeapon;
import com.tophattowl.dungeonsofvetir.game.combat.AttackType;
import com.tophattowl.dungeonsofvetir.game.combat.DamageType;
import com.tophattowl.dungeonsofvetir.game.combat.ElementType;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackContext;
import com.tophattowl.dungeonsofvetir.game.combat.context.MeleeAttackResult;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;
import com.tophattowl.dungeonsofvetir.game.items.Item;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.*;

public class MeleeAttackCalculator implements AttackCalculator<MeleeAttackResult, MeleeAttackContext> {

//    private List<EquipmentComponent.EquipmentSlot> wielding;
    private Item mainHandItem;
    private Item offHandItem;

    @Override
    public List<MeleeAttackResult> calculate(MeleeAttackContext context, GameWorld gameWorld) {
        MeleeAttackResult result = new MeleeAttackResult();

        Entity attacker = context.getAttacker();

        // temp placeholder
        MeleeAttackResult resulttt = new MeleeAttackResult();
        BodyPart targetedPart = context.getTarget().getComponent(BodyComponent.class).getRandomBodyPart();
        resulttt.setBodyPart(targetedPart);
        resulttt.addDamage(new Damage(10, ElementType.PHYSICAL, DamageType.SLASHING));

        return List.of(resulttt);



//        if (!attacker.hasComponent(EquipmentComponent.class)) {
//            return calculateNoEquipment(context, gameWorld);
//        }
//
//        EquipmentComponent attackerEquipment = attacker.getComponent(EquipmentComponent.class);
//
//        mainHandItem = attackerEquipment.getMainHandSlot().item;
//        offHandItem = attackerEquipment.getOffHandSlot().item;
//
//        if (mainHandItem.equals(offHandItem)) {
//            return calculateTwoHanded(context, gameWorld);
//        } else {
//            return calculateOneHanded(context, gameWorld);
//        }

//        Entity target = context.getTarget();

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




//        HealthComponent targetHealthComp = target.getComponent(HealthComponent.class);
//        DefensiveStatsComponent targetDefence = target.getComponent(DefensiveStatsComponent.class);
//        BodyComponent targetBodyComp = target.getComponent(BodyComponent.class);
//        BodyPart targetedBodyPart = targetBodyComp.getRandomBodyPart();
//
//        result.setBodyPart(targetedBodyPart);
//
//        return List.of(result);
    }

    private List<MeleeAttackResult> calculateTwoHanded(MeleeAttackContext context, GameWorld gameWorld) {

        return List.of();
    }

    private List<MeleeAttackResult> calculateOneHanded(MeleeAttackContext context, GameWorld gameWorld) {

        return List.of();
    }

    private List<MeleeAttackResult> calculateNoEquipment(MeleeAttackContext context, GameWorld gameWorld) {
        List<MeleeAttackResult> results = new ArrayList<>();
        Entity attacker = context.getAttacker();
        Entity target = context.getTarget();

        NaturalWeaponsComponent naturalWeaponsComp = attacker.getComponent(NaturalWeaponsComponent.class);
        OffensiveStatsComponent attackerOffense = attacker.getComponent(OffensiveStatsComponent.class);
        DefensiveStatsComponent targetDefense = target.getComponent(DefensiveStatsComponent.class);

        for (NaturalWeapon natWeapon : naturalWeaponsComp.getNaturalWeaponsSorted()) {

        }

        return results;
    }


    private boolean accuracyCheck(int attackerAcc, int targetAcc) {
        return true;
    }

    private boolean blockCheck(float blockChance) {
        return true;
    }

    private boolean counterCheck(float counterChance) {
        return true;
    }

    @Override
    public AttackType getType() {
        return AttackType.MELEE;
    }
}
