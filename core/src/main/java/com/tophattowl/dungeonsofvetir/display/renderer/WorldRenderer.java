package com.tophattowl.dungeonsofvetir.display.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tophattowl.dungeonsofvetir.display.tilesets.Tileset;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.FovComponent;
import com.tophattowl.dungeonsofvetir.game.ECS.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.ECS.components.RenderableComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.Tile;

import java.util.Comparator;
import java.util.List;

public class WorldRenderer {
    private static final float EXPLORED_SHADE = 0.35f;

    private final SpriteBatch batch;
    private final Tileset tileset;
    private FovOverlayRenderer fovOverlayRenderer;

    public WorldRenderer(SpriteBatch batch, Tileset tileset) {
        this.batch = batch;
        this.tileset = tileset;
    }

    public void setFovOverlayRenderer(FovOverlayRenderer fovOverlayRenderer) {
        this.fovOverlayRenderer = fovOverlayRenderer;
    }

    /**
     * Renders the world, called between batch.begin() and batch.end()
     * Camera should already be applied before this is called
     * @param world  game state to read from
     * @param camera used to cull tiles outside the viewport
     */
    public void render(GameWorld world, OrthographicCamera camera) {
        Level level = world.getCurrentLevel();

        if (level == null) return;

        renderTiles(level, camera);
        renderEntities(world);

    }

    private void renderTiles(Level level, OrthographicCamera camera) {
        // calc which tiles are within the camera's view (culling)
        int tw = Tileset.TILE_W;
        int th = Tileset.TILE_H;

        // cam bounds in pixels
        float camLeft   = camera.position.x - camera.viewportWidth  / 2f;
        float camRight  = camera.position.x + camera.viewportWidth  / 2f;
        float camBottom = camera.position.y - camera.viewportHeight / 2f;
        float camTop    = camera.position.y + camera.viewportHeight / 2f;

        // convert cam bounds to tile space
        int minTileX = Math.max(0, (int)(camLeft / tw) - 1);
        int maxTileX = Math.min(Level.WIDTH - 1, (int)(camRight / tw) + 1);

        // y is flipped
        int minTileY = Math.max(0, (int)((Level.HEIGHT - 1) - camTop / th) - 1);
        int maxTileY = Math.min(Level.HEIGHT - 1, (int)((Level.HEIGHT - 1) - camBottom / th) + 1);


//        batch.setColor(Color.WHITE);
        for (int x = minTileX; x <= maxTileX; x++) {
            for (int y = minTileY; y <= maxTileY; y++) {
                Tile tile = level.getTile(x, y);
                TextureRegion region = tileset.getTile(tile.type, tile.variant);
                float screenX = x * tw;
                float screenY = (Level.HEIGHT - 1 - y) * th;
                batch.setColor(Color.WHITE);
                batch.draw(region, screenX, screenY, tw, th);
            }
        }
    }

    private void renderEntities(GameWorld world) {
        Entity player = world.getPlayer();
        FovComponent fovComp = player.getComponent(FovComponent.class);


        List<Entity> renderables = world.querry(PositionComponent.class, RenderableComponent.class);
        renderables.sort(Comparator.comparingInt(
            e -> e.getComponent(RenderableComponent.class).renderOrder
        ));

        int tw = Tileset.TILE_W;
        int th = Tileset.TILE_H;

        batch.setColor(Color.WHITE);
        for (Entity entity : renderables) {
            if (fovOverlayRenderer != null && !fovOverlayRenderer.shouldRenderEntity(entity, fovComp)) {
                continue;
            }

            PositionComponent pos = entity.getComponent(PositionComponent.class);
            RenderableComponent renderable = entity.getComponent(RenderableComponent.class);
            TextureRegion region = tileset.getSprite(renderable.spriteId);
            float screenX = pos.getX() * tw;
            float screenY = (Level.HEIGHT - 1 - pos.getY()) * th;
            batch.draw(region, screenX, screenY, tw, th);
        }
    }
}
