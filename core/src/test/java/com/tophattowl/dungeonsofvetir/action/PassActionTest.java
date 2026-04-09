package com.tophattowl.dungeonsofvetir.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.ActionType;
import com.tophattowl.dungeonsofvetir.game.action.PassAction;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PassActionTest {

    @Test
    void constructor_SetsPossibleTrue() {
        Entity owner = new Entity();
        PassAction action = new PassAction(owner);
        assertFalse(action.notPossible());
    }

    @Test
    void constructor_SetsOwner() {
        Entity owner = new Entity();
        PassAction action = new PassAction(owner);
        assertSame(owner, action.getOwner());
    }

    @Test
    void constructor_SetsActionTypePass() {
        Entity owner = new Entity();
        PassAction action = new PassAction(owner);
        assertEquals(ActionType.PASS, action.getActionType());
    }

    @Test
    void constructor_SetsCost100() {
        Entity owner = new Entity();
        PassAction action = new PassAction(owner);
        assertEquals(100, action.getCost());
    }

    @Test
    void prepare_ReturnsThis() {
        Entity owner = new Entity();
        PassAction action = new PassAction(owner);
        PassAction result = (PassAction) action.prepare(null);
        assertSame(action, result);
    }

    @Test
    void execute_SetsSuccessTrue() {
        Entity owner = new Entity();
        PassAction action = new PassAction(owner);
        action.execute(null);
        assertTrue(action.isSuccess());
    }

    @Test
    void execute_ReturnsThis() {
        Entity owner = new Entity();
        PassAction action = new PassAction(owner);
        PassAction result = (PassAction) action.execute(null);
        assertSame(action, result);
    }
}
