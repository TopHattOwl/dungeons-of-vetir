package com.tophattowl.dungeonsofvetir.display.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tophattowl.dungeonsofvetir.display.tilesets.Tileset;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.util.dijkstra.DijkstraMapType;
import com.tophattowl.dungeonsofvetir.util.dijkstra.maps.DijkstraMap;

public class DijkstraOverlayRenderer {
    private static final float MAX_DISPLAY_VALUE = 100f;
    private static final float TEXT_SCALE = 0.8f;

    private GameWorld gameWorld;
    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;
    private final GlyphLayout glyphLayout;

    private boolean enabled = false;
    private DijkstraMapType mapType = DijkstraMapType.PLAYER;

    public DijkstraOverlayRenderer(SpriteBatch batch, BitmapFont font) {
        this.batch = batch;
        this.font = font;
        this.shapes = new ShapeRenderer();
        this.glyphLayout = new GlyphLayout();
    }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isEnabled() { return enabled; }

    public void setMapType(DijkstraMapType mapType) { this.mapType = mapType; }
    public DijkstraMapType getMapType() { return mapType; }

    public void toggle() {
        enabled = !enabled;
    }

    public void render(OrthographicCamera camera) {
        if (!enabled) return;
        if (gameWorld.dijkstraMapManager == null) return;

        int[][] map = gameWorld.dijkstraMapManager.getMap(mapType);
        if (map.length == 0) return;

        int tw = Tileset.TILE_W;
        int th = Tileset.TILE_H;

        float camLeft   = camera.position.x - camera.viewportWidth  / 2f;
        float camRight  = camera.position.x + camera.viewportWidth  / 2f;
        float camBottom = camera.position.y - camera.viewportHeight / 2f;
        float camTop    = camera.position.y + camera.viewportHeight / 2f;

        int minTileX = Math.max(0, (int)(camLeft / tw) - 1);
        int maxTileX = Math.min(Level.WIDTH  - 1, (int)(camRight / tw) + 1);
        int minTileY = Math.max(0, (int)((Level.HEIGHT - 1) - camTop    / th) - 1);
        int maxTileY = Math.min(Level.HEIGHT - 1, (int)((Level.HEIGHT - 1) - camBottom / th) + 1);

        com.badlogic.gdx.Gdx.gl.glEnable(GL20.GL_BLEND);
        com.badlogic.gdx.Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        for (int x = minTileX; x <= maxTileX; x++) {
            for (int y = minTileY; y <= maxTileY; y++) {
                float screenX = x * tw;
                float screenY = (Level.HEIGHT - 1 - y) * th;

                int value = map[x][y];
                if (value == DijkstraMap.OBSTACLE_VALUE) {
                    shapes.setColor(1f, 0.3f, 0.3f, 0.6f);
                    shapes.rect(screenX, screenY, tw, th);
                } else {
                    float intensity = Math.min(1f, value / MAX_DISPLAY_VALUE);
                    Color color = new Color(0.2f, 0.5f + (1f - intensity) * 0.5f, 0.8f, 0.5f);
                    shapes.setColor(color);
                    shapes.rect(screenX, screenY, tw, th);
                }
            }
        }

        shapes.end();
        com.badlogic.gdx.Gdx.gl.glDisable(GL20.GL_BLEND);

        font.getData().setScale(TEXT_SCALE);

        for (int x = minTileX; x <= maxTileX; x++) {
            for (int y = minTileY; y <= maxTileY; y++) {
                float screenX = x * tw;
                float screenY = (Level.HEIGHT - 1 - y) * th;

                int value = map[x][y];
                if (value == DijkstraMap.OBSTACLE_VALUE) {
                    font.setColor(1f, 0.2f, 0.2f, 1f);
                    glyphLayout.setText(font, "w");
                } else {
                    font.setColor(1f, 1f, 1f, 0.9f);
                    String text = String.valueOf(value);
                    glyphLayout.setText(font, text);
                }

                float textX = screenX + (tw - glyphLayout.width) / 2f;
                float textY = screenY + (th + glyphLayout.height) / 2f;
                batch.begin();
                font.draw(batch, glyphLayout, textX, textY);
                batch.end();
            }
        }

        font.getData().setScale(1f);
    }

    public void setGameWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void dispose() {
        shapes.dispose();
    }
}
