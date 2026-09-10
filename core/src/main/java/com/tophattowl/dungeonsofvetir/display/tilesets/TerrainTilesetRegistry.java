package com.tophattowl.dungeonsofvetir.display.tilesets;

import com.badlogic.gdx.utils.Disposable;
import com.tophattowl.dungeonsofvetir.display.assets.AssetPaths;
import com.tophattowl.dungeonsofvetir.display.assets.TextureRegistry;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.TileTheme;

import java.util.EnumMap;
import java.util.Map;

/**
 * Resolves a {@link TileTheme} to its terrain tileset. Themes with art use
 * {@link AssetTileset}; the rest fall back to the procedural {@link PaletteTileset}.
 * <p>
 * Owns every tileset it creates (palette tilesets release their generated textures here).
 */
public class TerrainTilesetRegistry implements Disposable {

    private final TextureRegistry textures;
    private final Map<TileTheme, Tileset> cache = new EnumMap<>(TileTheme.class);

    public TerrainTilesetRegistry(TextureRegistry textures) {
        this.textures = textures;
    }

    public Tileset get(TileTheme theme) {
        return cache.computeIfAbsent(theme, this::create);
    }

    private Tileset create(TileTheme theme) {
        return switch (theme) {
            case CAVES_DANK -> new AssetTileset(
                textures, AssetPaths.CAVE_FLOOR, AssetPaths.CAVE_WALL, AssetPaths.STAIR_DOWN
            );
            case GROVE, RUINS_FORTRESS, REST -> new PaletteTileset(theme);
        };
    }

    @Override
    public void dispose() {
        cache.values().forEach(Tileset::dispose);
        cache.clear();
    }
}
