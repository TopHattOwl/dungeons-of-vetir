package com.tophattowl.dungeonsofvetir.game.combat.context;

import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;

import java.util.List;

/**
 * Output for AttackCalculators
 */
public abstract class AttackResult {
    boolean hit;
    boolean blocked;
    BodyPart bodyPart;
    List<Damage> damages;
}
