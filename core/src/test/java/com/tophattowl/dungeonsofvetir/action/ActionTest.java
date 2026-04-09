package com.tophattowl.dungeonsofvetir.action;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.action.Action;
import com.tophattowl.dungeonsofvetir.game.action.ActionType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    @Test
    void constructor_SetsOwner() {
        Entity owner = new Entity();
        TestAction action = new TestAction(ActionType.MOVE, owner);
        assertSame(owner, action.getOwner());
    }

    @Test
    void constructor_SetsActionType() {
        Entity owner = new Entity();
        TestAction action = new TestAction(ActionType.MOVE, owner);
        assertEquals(ActionType.MOVE, action.getActionType());
    }

    @Test
    void constructor_SetsCost() {
        Entity owner = new Entity();
        TestAction moveAction = new TestAction(ActionType.MOVE, owner);
        TestAction passAction = new TestAction(ActionType.PASS, owner);
        TestAction equipAction = new TestAction(ActionType.EQUIP, owner);

        assertEquals(100, moveAction.getCost());
        assertEquals(100, passAction.getCost());
        assertEquals(50, equipAction.getCost());
    }

    @Test
    void constructor_InitializesPossibleFalse() {
        Entity owner = new Entity();
        TestAction action = new TestAction(ActionType.MOVE, owner);
        assertTrue(action.notPossible());
    }

    @Test
    void constructor_InitializesSuccessFalse() {
        Entity owner = new Entity();
        TestAction action = new TestAction(ActionType.MOVE, owner);
        assertFalse(action.isSuccess());
    }

    @Test
    void possible_SetsPossibleTrue() {
        Entity owner = new Entity();
        TestAction action = new TestAction(ActionType.MOVE, owner);
        action.possible();
        assertFalse(action.notPossible());
    }

    @Test
    void success_SetsSuccessTrue() {
        Entity owner = new Entity();
        TestAction action = new TestAction(ActionType.MOVE, owner);
        action.success();
        assertTrue(action.isSuccess());
    }

    @Test
    void notPossible_WhenPossibleFalse() {
        Entity owner = new Entity();
        TestAction action = new TestAction(ActionType.MOVE, owner);
        assertTrue(action.notPossible());
    }

    @Test
    void notPossible_WhenPossibleTrue() {
        Entity owner = new Entity();
        TestAction action = new TestAction(ActionType.MOVE, owner);
        action.possible();
        assertFalse(action.notPossible());
    }

    @Test
    void allActionTypes_HaveBaseCosts() {
        for (ActionType type : ActionType.values()) {
            assertTrue(type.getBaseCost() >= 0, type.name() + " should have a valid base cost");
        }
    }

    @Test
    void actionType_None_HasZeroCost() {
        assertEquals(0, ActionType.NONE.getBaseCost());
    }

    private static class TestAction extends Action {
        public TestAction(ActionType type, Entity owner) {
            super(type, owner);
        }

        @Override
        public Action prepare(com.tophattowl.dungeonsofvetir.game.world.GameWorld gameWorld) {
            return this;
        }

        @Override
        public Action execute(com.tophattowl.dungeonsofvetir.game.world.GameWorld gameWorld) {
            return this;
        }
    }
}
