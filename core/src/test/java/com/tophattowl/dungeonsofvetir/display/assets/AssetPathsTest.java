package com.tophattowl.dungeonsofvetir.display.assets;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.ActorId;
import com.tophattowl.dungeonsofvetir.game.actors.components.RenderableComponent;
import com.tophattowl.dungeonsofvetir.game.factory.actors.ActorRegistry;
import com.tophattowl.dungeonsofvetir.game.factory.actors.EntityFactory;
import com.tophattowl.dungeonsofvetir.game.factory.actors.component_specs.RenderableSpec;
import com.tophattowl.dungeonsofvetir.game.world.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssetPathsTest {

    @Test
    void expectedSpritesAreRegistered() {
        assertTrue(AssetPaths.hasSprite("player_knight"));
        for (String id : new String[]{"iron_worm", "fireant", "mask", "stone_cyclops", "tooth_fairy"}) {
            assertTrue(AssetPaths.hasSprite(id), "missing sprite id: " + id);
        }
    }

    @Test
    void allRegisteredSpritesHavePaths() {
        for (String id : AssetPaths.spriteIds()) {
            assertNotNull(AssetPaths.spritePath(id), "no path for sprite id: " + id);
        }
    }

    @Test
    void unknownSpriteHasNoPath() {
        assertFalse(AssetPaths.hasSprite("does_not_exist"));
        assertNull(AssetPaths.spritePath("does_not_exist"));
    }

    @Test
    void playerUsesRegisteredSprite() {
        Entity player = EntityFactory.makePlayer(new Point(0, 0));
        try {
            String spriteId = player.getComponent(RenderableComponent.class).spriteId;
            assertTrue(AssetPaths.hasSprite(spriteId), "player sprite id not registered: " + spriteId);
        } finally {
            player.dispose();
        }
    }

    @Test
    void ironWormUsesRegisteredSprite() {
        String spriteId = ActorRegistry.get(ActorId.IRON_WORM).baseSpecs().stream()
            .filter(RenderableSpec.class::isInstance)
            .map(RenderableSpec.class::cast)
            .map(RenderableSpec::spriteId)
            .findFirst()
            .orElseThrow();

        assertEquals("iron_worm", spriteId);
        assertTrue(AssetPaths.hasSprite(spriteId));
    }
}
