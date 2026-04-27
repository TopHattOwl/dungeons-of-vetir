package com.tophattowl.dungeonsofvetir.game.event.events.combat;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.combat.damage.Damage;
import com.tophattowl.dungeonsofvetir.game.event.events.Event;
import com.tophattowl.dungeonsofvetir.game.items.Item;

import java.util.List;

public record MeleeAttackHitEvent(
    Entity attacker,
    Entity target,
    BodyPart hitBodyPart,
    List<Damage> damages,
    Item usedWeapon
) implements Event {
}
