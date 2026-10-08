package com.tophattowl.dungeonsofvetir.display.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.tophattowl.dungeonsofvetir.display.tilesets.Tileset;

public class CameraController {
    private final OrthographicCamera camera;
    private final int viewportW;
    private final int viewportH;

    public CameraController(int viewportW, int viewportH) {
        this.viewportW = viewportW;
        this.viewportH = viewportH;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, viewportW, viewportH);
        camera.update();
    }

    /**
     * Center the camera on the given tile within a level of the given size.
     * Clamped so the viewport never shows outside the level bounds; levels smaller
     * than the viewport are simply centered.
     */
    public void centerOn(int tileX, int tileY, int levelWidth, int levelHeight) {
        // convert tile to pixel pos
        // Y is flipped: tile y=0 is top of level, world y=0 is bottom
        float worldX = tileX * Tileset.TILE_W + Tileset.TILE_W / 2f;
        float worldY = (levelHeight - 1 - tileY) * Tileset.TILE_H + Tileset.TILE_H / 2f;

        float levelPixelW = levelWidth * Tileset.TILE_W;
        float levelPixelH = levelHeight * Tileset.TILE_H;

        float halfW = viewportW / 2f;
        float halfH = viewportH / 2f;

        float clampedX = levelPixelW <= viewportW
            ? levelPixelW / 2f
            : Math.max(halfW, Math.min(levelPixelW - halfW, worldX));
        float clampedY = levelPixelH <= viewportH
            ? levelPixelH / 2f
            : Math.max(halfH, Math.min(levelPixelH - halfH, worldY));

        camera.position.set(clampedX, clampedY, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
