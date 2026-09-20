package com.tophattowl.dungeonsofvetir.display.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tophattowl.dungeonsofvetir.display.assets.AssetPaths;
import com.tophattowl.dungeonsofvetir.display.assets.TextureRegistry;
import com.tophattowl.dungeonsofvetir.game.debug.DebugLogger;

import java.util.HashSet;
import java.util.Set;

/**
 * Theme-independent sprite lookup for entities, items and npcs. Keyed by the
 * {@code spriteId} stored in {@code RenderableComponent}; unregistered ids fall
 * back to the magenta "missing" region (logged once).
 */
public class SpriteLibrary {

    private final TextureRegistry textures;
    private final Set<String> warnedUnregistered = new HashSet<>();

    public SpriteLibrary(TextureRegistry textures) {
        this.textures = textures;
    }

    public TextureRegion get(String spriteId) {
        String path = AssetPaths.spritePath(spriteId);
        if (path == null) {
            if (warnedUnregistered.add(spriteId)) {
                DebugLogger.log(DebugLogger.Category.FACTORY, DebugLogger.Level.WARNING,
                    "SpriteLibrary", "No asset registered for sprite id: " + spriteId);
            }
            return textures.missing();
        }
        return textures.region(path);
    }
}
