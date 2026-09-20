package com.tophattowl.dungeonsofvetir.display.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.tophattowl.dungeonsofvetir.display.tilesets.Tileset;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Loads and caches textures and regions by path. Every texture uses Nearest filtering.
 * Owns all textures it loads; {@link #dispose()} releases them.
 * <p>
 * Missing paths never crash: they resolve to a generated magenta {@link #missing()} region.
 */
public class TextureRegistry implements Disposable {

    private final Map<String, Texture> textures = new HashMap<>();
    private final Map<String, TextureRegion> regions = new HashMap<>();
    private final Map<String, TextureRegion[]> strips = new HashMap<>();
    private final Set<String> warnedMissing = new HashSet<>();

    private Texture missingTexture;
    private TextureRegion missingRegion;

    public TextureRegion region(String path) {
        if (path == null) return missing();

        TextureRegion cached = regions.get(path);
        if (cached != null) return cached;

        if (!Gdx.files.internal(path).exists()) {
            warnOnce(path);
            regions.put(path, missing());
            return missingRegion;
        }

        Texture texture = new Texture(path);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        textures.put(path, texture);

        TextureRegion region = new TextureRegion(texture);
        regions.put(path, region);
        return region;
    }

    /**
     * Splits a single-row strip into its frames. A missing/broken image yields a
     * one-element array containing the magenta fallback.
     */
    public TextureRegion[] strip(String path, int frameWidth, int frameHeight) {
        TextureRegion[] cached = strips.get(path);
        if (cached != null) return cached;

        TextureRegion[][] grid = region(path).split(frameWidth, frameHeight);
        TextureRegion[] frames = grid[0];
        strips.put(path, frames);
        return frames;
    }

    /**
     * @return a generated magenta tile used when a texture or sprite is unavailable
     */
    public TextureRegion missing() {
        if (missingRegion == null) {
            Pixmap px = new Pixmap(Tileset.TILE_W, Tileset.TILE_H, Pixmap.Format.RGBA8888);
            px.setColor(Color.MAGENTA);
            px.fill();
            px.setColor(Color.BLACK);
            px.drawRectangle(0, 0, Tileset.TILE_W, Tileset.TILE_H);

            missingTexture = new Texture(px);
            missingTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
            px.dispose();

            missingRegion = new TextureRegion(missingTexture);
        }
        return missingRegion;
    }

    private void warnOnce(String path) {
        if (warnedMissing.add(path)) {
            DebugLogger.log(DebugLogger.Category.FACTORY, DebugLogger.Level.WARNING,
                "TextureRegistry", "Missing texture: " + path);
        }
    }

    @Override
    public void dispose() {
        textures.values().forEach(Texture::dispose);
        textures.clear();
        regions.clear();
        strips.clear();

        if (missingTexture != null) {
            missingTexture.dispose();
            missingTexture = null;
            missingRegion = null;
        }
    }
}
