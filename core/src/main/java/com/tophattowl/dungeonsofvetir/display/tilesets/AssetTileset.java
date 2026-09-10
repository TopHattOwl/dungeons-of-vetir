package com.tophattowl.dungeonsofvetir.display.tilesets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tophattowl.dungeonsofvetir.display.assets.TextureRegistry;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

/**
 * Asset-backed terrain tileset. Floor and wall are horizontal strips of 16x24
 * frames; stairs currently share a single region for both directions.
 * <p>
 * Textures are owned by the {@link TextureRegistry}, so this class does not dispose them.
 */
public class AssetTileset implements Tileset {

    private final TextureRegion[] floorFrames;
    private final TextureRegion[] wallFrames;
    private final TextureRegion stairs;

    public AssetTileset(TextureRegistry textures, String floorPath, String wallPath, String stairsPath) {
        this.floorFrames = textures.strip(floorPath, TILE_W, TILE_H);
        this.wallFrames = textures.strip(wallPath, TILE_W, TILE_H);
        this.stairs = textures.region(stairsPath);
    }

    @Override
    public TextureRegion getTile(TileType type, int variant) {
        return switch (type) {
            case FLOOR -> pick(floorFrames, variant);
            case WALL, BORDER_WALL -> pick(wallFrames, variant);
            case STAIRS_UP, STAIRS_DOWN -> stairs;
            // No door art yet; caves never place doors, ruins still uses the palette fallback.
            case DOOR_OPEN, DOOR_CLOSED -> pick(wallFrames, variant);
        };
    }

    private TextureRegion pick(TextureRegion[] frames, int variant) {
        if (frames.length == 0) return null;
        return frames[Math.floorMod(variant, frames.length)];
    }

    @Override
    public void dispose() {
        // Textures are owned by the TextureRegistry.
    }
}
