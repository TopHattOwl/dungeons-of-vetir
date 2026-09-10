package com.tophattowl.dungeonsofvetir.display.assets;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Central, GL-free lookup of asset paths. Safe to use from tests.
 * <p>
 * Sprite ids double as the keys stored in {@code RenderableComponent.spriteId};
 * terrain paths are referenced explicitly by {@code TerrainTilesetRegistry}.
 */
public final class AssetPaths {

    // Terrain (currently only the dank cave has art)
    public static final String CAVE_FLOOR = "sprites/floor/caves/generic_cave_floor.png";
    public static final String CAVE_WALL  = "sprites/wall/cave/generic_cave_wall.png";
    public static final String STAIR_DOWN = "sprites/stair/stair_down.png";

    private static final Map<String, String> SPRITES = new LinkedHashMap<>();

    private AssetPaths() {}

    static {
        register("player_knight", "sprites/player/player_knight.png");

        register("iron_worm", "sprites/monsters/alive/iron_worm.png");
        register("fireant", "sprites/monsters/alive/fireant.png");
        register("mask", "sprites/monsters/alive/mask.png");
        register("stone_cyclops", "sprites/monsters/alive/stone_cyclops.png");
        register("tooth_fairy", "sprites/monsters/alive/tooth_fairy.png");
    }

    private static void register(String spriteId, String path) {
        SPRITES.put(spriteId, path);
    }

    /**
     * @return the file path for a sprite id, or {@code null} if none is registered
     */
    public static String spritePath(String spriteId) {
        return SPRITES.get(spriteId);
    }

    public static boolean hasSprite(String spriteId) {
        return SPRITES.containsKey(spriteId);
    }

    public static Set<String> spriteIds() {
        return Set.copyOf(SPRITES.keySet());
    }
}
