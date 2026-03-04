package com.tophattowl.dungeonsofvetir.display.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.tophattowl.dungeonsofvetir.display.tilesets.Tileset;
import com.tophattowl.dungeonsofvetir.game.world.Level;

public class CameraController {
    private final OrthographicCamera camera;
    private final int viewportW;
    private final int viewportH;

    private final float levelPixelW;
    private final float levelPixelH;

    public CameraController(int viewportW, int viewportH) {
        this.viewportW = viewportW;
        this.viewportH = viewportH;
        this.levelPixelW = Level.WIDTH  * Tileset.TILE_W;
        this.levelPixelH = Level.HEIGHT * Tileset.TILE_H;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportW, viewportH);
        camera.update();
    }

    /**
     * Center the camera on the given tile.
     * Clamped so the viewport never shows outside the level bounds.
     */
    public void centerOn(int tileX, int tileY) {
        // convert tile to pixel pos
        // Y is flipped: tile y=0 is top of level, world y=0 is bottom
        float worldX = tileX * Tileset.TILE_W + Tileset.TILE_W / 2f;
        float worldY = (Level.HEIGHT - 1 - tileY) * Tileset.TILE_H + Tileset.TILE_H / 2f;

        // clamp
        float halfW = viewportW / 2f;
        float halfH = viewportH / 2f;

        float clampedX = Math.max(halfW, Math.min(levelPixelW - halfW, worldX));
        float clampedY = Math.max(halfH, Math.min(levelPixelH - halfH, worldY));

        camera.position.set(clampedX, clampedY, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void resize(int width, int height) {
        camera.update();
    }
}
