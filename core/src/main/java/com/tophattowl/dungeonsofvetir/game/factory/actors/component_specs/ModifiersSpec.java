package com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.ModifiersComponent;
import com.tophattowl.dungeonsofvetir.game.stats.Modifier;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

/**
 * Innate/species modifiers an actor carries from birth (before gear, statuses or expertise).
 */
public record ModifiersSpec(
    Modifier... modifiers
) implements ActorComponentSpec<ModifiersComponent> {

    @Override
    public Class<ModifiersComponent> getComponentType() {
        return ModifiersComponent.class;
    }

    @Override
    public ModifiersComponent build(Entity entity, GameWorld gameWorld) {
        return new ModifiersComponent(modifiers);
    }
}
