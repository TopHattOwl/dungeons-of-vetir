package com.tophattowl.dungeonsofvetir.game.status;

import com.badlogic.gdx.utils.Disposable;
import com.tophattowl.dungeonsofvetir.game.action.ActionType;
import com.tophattowl.dungeonsofvetir.game.event.EventSubscriptions;
import com.tophattowl.dungeonsofvetir.game.event.events.ActionCompletedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.combat.MeleeAttackHitEvent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;

import java.util.Set;

/**
 * Applies the suppression markers that hold back natural regeneration: a target that was just
 * hit gains {@code RECENTLY_WOUNDED}, and an actor that performs a non-trivial action gains
 * {@code EXERTED}. Kept separate from {@link StatusEffectSystem} to keep that system generic.
 */
public class RegenRules implements Disposable {

    private static final Set<ActionType> SAFE_ACTIONS = Set.of(
        ActionType.PASS, ActionType.MOVE, ActionType.PICKUP, ActionType.DROP, ActionType.NONE
    );

    private static final int WOUNDED_TURNS = 12;
    private static final int EXERTED_TURNS = 8;

    private final GameWorld world;
    private final EventSubscriptions subscriptions = new EventSubscriptions();

    public RegenRules(GameWorld world) {
        this.world = world;
        subscriptions.on(MeleeAttackHitEvent.class, this::onDamaged);
        subscriptions.on(ActionCompletedEvent.class, this::onActionCompleted);
    }

    private void onDamaged(MeleeAttackHitEvent event) {
        world.statusEffects().apply(event.target(), StatusEffectId.RECENTLY_WOUNDED,
            "regen", 1, WOUNDED_TURNS);
    }

    private void onActionCompleted(ActionCompletedEvent event) {
        if (SAFE_ACTIONS.contains(event.action().getActionType())) return;
        world.statusEffects().apply(event.entity(), StatusEffectId.EXERTED,
            "regen", 1, EXERTED_TURNS);
    }

    @Override
    public void dispose() {
        subscriptions.unsubscribeAll();
    }
}
