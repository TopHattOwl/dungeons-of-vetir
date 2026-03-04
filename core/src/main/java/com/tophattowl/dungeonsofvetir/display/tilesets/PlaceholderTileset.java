package com.tophattowl.dungeonsofvetir.display.tilesets;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

import java.util.HashMap;
import java.util.Map;

/**
 * Color scheme:
 *   Border wall  — dark grey
 *   Wall       — mid grey
 *   Floor      — brown variants
 *   Stairs     — yellow / dark yellow
 *   Player     — bright white
 */
public class PlaceholderTileset implements Tileset {
    private final Map<String, Texture> textures = new HashMap<>();
    private final Map<String, TextureRegion> regions = new HashMap<>();


    private static final int FLOOR_VARIANTS = 4;
    private static final int WALL_VARIANTS = 4;
    public PlaceholderTileset() {
        // border wall
        add("border_wall", new Color(0.20f, 0.20f, 0.22f, 1f));

        // floor variants
        float[] browns = {0.30f, 0.28f, 0.32f, 0.27f};
        for (int i = 0; i < FLOOR_VARIANTS; i++) {
            float b = browns[i];
            add("floor_" + i, new Color(b, b * 0.7f, b * 0.4f, 1f));
        }

        // wall variants
        for (int i = 0; i < WALL_VARIANTS; i++) {
            add("wall_" + i, new Color(0.45f, 0.40f, 0.30f, 1f));
        }

        // stairs
        add("stairs_down", new Color(1.0f, 0.85f, 0.0f, 1f));
        add("stairs_up",   new Color(0.8f, 0.70f, 0.0f, 1f));

        // door
        add("door_open",   new Color(0.55f, 0.35f, 0.15f, 1f));
        add("door_closed", new Color(0.40f, 0.25f, 0.10f, 1f));

        // entities
        add("player", new Color(1.0f, 1.0f, 1.0f, 1f));

        add("unknown", new Color(1.0f, 0.0f, 1.0f, 1f));


    }

    private void add(String key, Color color) {
        Pixmap px = new  Pixmap(TILE_W, TILE_H, Pixmap.Format.RGBA8888);
        px.setColor(color);
        px.fill();

        // draw a 1px darker border so tiles are visually separated
        px.setColor(color.r * 0.6f, color.g * 0.6f, color.b * 0.6f, 1f);
        px.drawRectangle(0, 0, TILE_W, TILE_H);

        Texture tex = new Texture(px);
        px.dispose();

        textures.put(key, tex);
        regions.put(key, new TextureRegion(tex));
    }

    @Override
    public TextureRegion getTile(TileType type, int variant) {
        String key = switch (type) {
            case FLOOR -> "floor_" + (variant % FLOOR_VARIANTS);
            case WALL -> "wall_" +  (variant % FLOOR_VARIANTS);
            case BORDER_WALL -> "border_wall";
            case STAIRS_UP -> "stairs_up";
            case STAIRS_DOWN -> "stairs_down";
            case DOOR_OPEN -> "door_open";
            case DOOR_CLOSED -> "door_closed";
        };
        return regions.getOrDefault(key, regions.get("unknown"));
    }

    @Override
    public TextureRegion getSprite(String spriteId) {
        return regions.getOrDefault(spriteId, regions.get("unknown"));
    }

    @Override
    public void dispose() {
        textures.values().forEach(Texture::dispose);
        textures.clear();
        regions.clear();
    }
}
