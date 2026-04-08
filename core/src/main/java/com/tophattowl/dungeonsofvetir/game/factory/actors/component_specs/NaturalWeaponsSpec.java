package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.body.BodyPart;
import com.tophattowl.dungeonsofvetir.game.actors.components.BodyComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.NaturalWeaponsComponent;
import com.tophattowl.dungeonsofvetir.game.actors.monsters.NaturalWeapon;

import java.util.*;

public record NaturalWeaponsSpec(
    Map<String, NaturalWeapon> naturalWeapons
) implements ActorComponentSpec<NaturalWeaponsComponent>{

    @Override
    public Class<NaturalWeaponsComponent> getComponentType() {
        return NaturalWeaponsComponent.class;
    }

    @Override
    public NaturalWeaponsComponent build(Entity entity) {
        Map<BodyPart, NaturalWeapon> compArg = new HashMap<>();

        for (Map.Entry<String, NaturalWeapon> entry : naturalWeapons.entrySet()) {
            BodyPart bodyPart = entity.getComponent(BodyComponent.class).getPartByName(entry.getKey());

            if (bodyPart == null) continue;

            compArg.put(bodyPart, entry.getValue());
        }
        return new NaturalWeaponsComponent(compArg);
    }
}
