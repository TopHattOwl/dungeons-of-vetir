package com.tophattowl.dungeonsofvetir.display.renderer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tophattowl.dungeonsofvetir.display.tilesets.Tileset;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.FovComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PlayerComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;

/**
 * Draws on top of the world renderer to apply FOV visibility.
 *
 * Three states per tile:
 *   Never explored  -> fully black (alpha 1.0)
 *   Explored but not visible -> dark overlay (alpha 0.6)
 *   Currently visible -> nothing drawn on top
 *
 * Also gates entity rendering non-player entities outside FOV are skipped
 * Call render() AFTER batch.end() and BEFORE HUD rendering
 */
public class FovOverlayRenderer {

    private static final float ALPHA_UNSEEN   = 1.0f;
    private static final float ALPHA_EXPLORED = 0.6f;

    private final ShapeRenderer shapes;
    private boolean enabled = true;

    public FovOverlayRenderer() {
        shapes = new ShapeRenderer();
    }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isEnabled() { return enabled; }

    public void render(GameWorld world, OrthographicCamera camera) {
        if (!enabled) return;

        Entity player = world.getPlayer();
        if (player == null) return;

        FovComponent fov = player.getComponent(FovComponent.class);
        if (fov == null) return;

        // TODO: put this shit into some helper class
        int tw = Tileset.TILE_W;
        int th = Tileset.TILE_H;

        // cull to camera view
        float camLeft   = camera.position.x - camera.viewportWidth  / 2f;
        float camRight  = camera.position.x + camera.viewportWidth  / 2f;
        float camBottom = camera.position.y - camera.viewportHeight / 2f;
        float camTop    = camera.position.y + camera.viewportHeight / 2f;

        int minTileX = Math.max(0, (int)(camLeft / tw) - 1);
        int maxTileX = Math.min(Level.WIDTH  - 1, (int)(camRight / tw) + 1);
        int minTileY = Math.max(0, (int)((Level.HEIGHT - 1) - camTop    / th) - 1);
        int maxTileY = Math.min(Level.HEIGHT - 1, (int)((Level.HEIGHT - 1) - camBottom / th) + 1);

        // enable blending so the explored overlay is transparent
        com.badlogic.gdx.Gdx.gl.glEnable(GL20.GL_BLEND);
        com.badlogic.gdx.Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        for (int x = minTileX; x <= maxTileX; x++) {
            for (int y = minTileY; y <= maxTileY; y++) {
                float screenX = x * tw;
                float screenY = (Level.HEIGHT - 1 - y) * th;

                if (!fov.isExplored(x, y)) {
                    shapes.setColor(0f, 0f, 0f, ALPHA_UNSEEN);
                    shapes.rect(screenX, screenY, tw, th);
                } else if (!fov.isVisible(x, y)) {
                    shapes.setColor(0f, 0f, 0f, ALPHA_EXPLORED);
                    shapes.rect(screenX, screenY, tw, th);
                }
                // visible -> draw nothing
            }
        }

        shapes.end();
        com.badlogic.gdx.Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    /**
     * Should this entity be rendered?
     * Player always renders, other entities only render if currently visible by player
     */
    public boolean shouldRenderEntity(Entity entity, FovComponent playerFov) {
        if (!enabled) return true;
        if (entity.hasComponent(PlayerComponent.class)) return true;
        if (playerFov == null) return true;
        PositionComponent pos = entity.getComponent(PositionComponent.class);
        if (pos == null) return true;
        return playerFov.isVisible(pos.getX(), pos.getY());
    }

    public void dispose() {
        shapes.dispose();
    }
}
