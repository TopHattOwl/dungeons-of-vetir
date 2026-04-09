package com.tophattowl.dungeonsofvetir.ECS;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.IdentityComponent;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.faction.Faction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void constructor_AssignsUniqueId() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();
        assertNotEquals(e1.id, e2.id);
    }

    @Test
    void constructor_AssignsIncrementingIds() {
        Entity e1 = new Entity();
        Entity e2 = new Entity();
        Entity e3 = new Entity();
        assertTrue(e1.id < e2.id);
        assertTrue(e2.id < e3.id);
    }

    @Test
    void addComponent_ReturnsEntity() {
        Entity entity = new Entity();
        IdentityComponent component = new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER);
        Entity result = entity.addComponent(component);
        assertSame(entity, result);
    }

    @Test
    void addComponent_StoresComponent() {
        Entity entity = new Entity();
        IdentityComponent component = new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER);
        entity.addComponent(component);
        assertSame(component, entity.getComponent(IdentityComponent.class));
    }

    @Test
    void addComponent_Overwrites() {
        Entity entity = new Entity();
        IdentityComponent component1 = new IdentityComponent("test1", ActorId.PLAYER, Faction.HUNTER);
        IdentityComponent component2 = new IdentityComponent("test2", ActorId.PLAYER, Faction.HUNTER);
        entity.addComponent(component1);
        entity.addComponent(component2);
        assertSame(component2, entity.getComponent(IdentityComponent.class));
    }

    @Test
    void getComponent_Existing() {
        Entity entity = new Entity();
        IdentityComponent component = new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER);
        entity.addComponent(component);
        assertSame(component, entity.getComponent(IdentityComponent.class));
    }

    @Test
    void getComponent_NotExisting() {
        Entity entity = new Entity();
        assertNull(entity.getComponent(IdentityComponent.class));
    }

    @Test
    void removeComponent_Existing() {
        Entity entity = new Entity();
        IdentityComponent component = new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER);
        entity.addComponent(component);
        entity.removeComponent(IdentityComponent.class);
        assertNull(entity.getComponent(IdentityComponent.class));
    }

    @Test
    void hasComponent_True() {
        Entity entity = new Entity();
        IdentityComponent component = new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER);
        entity.addComponent(component);
        assertTrue(entity.hasComponent(IdentityComponent.class));
    }

    @Test
    void hasComponent_False() {
        Entity entity = new Entity();
        assertFalse(entity.hasComponent(IdentityComponent.class));
    }

    @Test
    void hasAllComponents_AllPresent() {
        Entity entity = new Entity();
        entity.addComponent(new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER));
        entity.addComponent(new com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent(0, 0));
        assertTrue(entity.hasAllComponents(IdentityComponent.class, com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent.class));
    }

    @Test
    void hasAllComponents_OneMissing() {
        Entity entity = new Entity();
        entity.addComponent(new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER));
        assertFalse(entity.hasAllComponents(IdentityComponent.class, com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent.class));
    }

    @Test
    void hasAllComponents_Empty() {
        Entity entity = new Entity();
        assertTrue(entity.hasAllComponents());
    }

    @Test
    void toString_WithIdentityComponent() {
        Entity entity = new Entity();
        entity.addComponent(new IdentityComponent("TestEntity", ActorId.PLAYER, Faction.HUNTER));
        String result = entity.toString();
        assertTrue(result.contains("TestEntity"));
    }

    @Test
    void toString_WithoutIdentityComponent() {
        Entity entity = new Entity();
        String result = entity.toString();
        assertTrue(result.contains("Entity #"));
        assertTrue(result.contains(String.valueOf(entity.id)));
    }

    @Test
    void getAllInfo_ReturnsComponentInfo() {
        Entity entity = new Entity();
        IdentityComponent identity = new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER);
        entity.addComponent(identity);
        String info = entity.getAllInfo();
        assertNotNull(info);
        assertTrue(info.length() > 0);
    }

    @Test
    void multipleComponents_CanBeRetrieved() {
        Entity entity = new Entity();
        IdentityComponent identity = new IdentityComponent("test", ActorId.PLAYER, Faction.HUNTER);
        var position = new com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent(5, 10);
        entity.addComponent(identity);
        entity.addComponent(position);

        assertSame(identity, entity.getComponent(IdentityComponent.class));
        assertSame(position, entity.getComponent(com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent.class));
    }
}
