package com.tophattowl.dungeonsofvetir.display.tilesets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.TileTheme;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

import java.util.HashMap;
import java.util.Map;

/**
 * Procedural fallback terrain tileset, palette-driven by a {@link TileTheme}.
 * Used for themes that don't have art yet. Entity sprites do not belong here.
 */
public class PaletteTileset implements Tileset {

    private static final int FLOOR_VARIANTS = 4;
    private static final int WALL_VARIANTS = 4;

    private final Map<String, Texture> textures = new HashMap<>();
    private final Map<String, TextureRegion> regions = new HashMap<>();

    private final TileTheme theme;

    public PaletteTileset(TileTheme theme) {
        this.theme = theme;
        rebuild();
    }

    private void rebuild() {
        Palette palette = paletteFor(theme);

        add("border_wall", palette.borderWall());

        for (int i = 0; i < FLOOR_VARIANTS; i++) {
            add("floor_" + i, shade(palette.floor(), variantFactor(i, FLOOR_VARIANTS, 0.06f)));
        }
        for (int i = 0; i < WALL_VARIANTS; i++) {
            add("wall_" + i, shade(palette.wall(), variantFactor(i, WALL_VARIANTS, 0.05f)));
        }

        add("stairs_up", palette.stairsUp());
        add("stairs_down", palette.stairsDown());
        add("door_open", palette.doorOpen());
        add("door_closed", palette.doorClosed());

        add("unknown", new Color(1f, 0f, 1f, 1f));
    }

    private float variantFactor(int index, int count, float step) {
        return 1f + (index - (count - 1) / 2f) * step;
    }

    private Palette paletteFor(TileTheme theme) {
        return switch (theme) {
            case CAVES_DANK -> new Palette(
                new Color(0.30f, 0.28f, 0.24f, 1f),
                new Color(0.45f, 0.40f, 0.30f, 1f),
                new Color(0.20f, 0.20f, 0.22f, 1f),
                new Color(0.80f, 0.70f, 0.00f, 1f),
                new Color(1.00f, 0.85f, 0.00f, 1f),
                new Color(0.55f, 0.35f, 0.15f, 1f),
                new Color(0.40f, 0.25f, 0.10f, 1f)
            );
            case GROVE -> new Palette(
                new Color(0.20f, 0.32f, 0.20f, 1f),
                new Color(0.16f, 0.28f, 0.22f, 1f),
                new Color(0.10f, 0.16f, 0.14f, 1f),
                new Color(0.85f, 0.75f, 0.30f, 1f),
                new Color(0.55f, 0.85f, 0.55f, 1f),
                new Color(0.45f, 0.30f, 0.55f, 1f),
                new Color(0.30f, 0.20f, 0.40f, 1f)
            );
            case RUINS_FORTRESS -> new Palette(
                new Color(0.35f, 0.34f, 0.33f, 1f),
                new Color(0.26f, 0.25f, 0.25f, 1f),
                new Color(0.15f, 0.15f, 0.16f, 1f),
                new Color(0.75f, 0.75f, 0.55f, 1f),
                new Color(0.85f, 0.85f, 0.65f, 1f),
                new Color(0.45f, 0.32f, 0.18f, 1f),
                new Color(0.32f, 0.22f, 0.12f, 1f)
            );
            case REST -> new Palette(
                new Color(0.34f, 0.30f, 0.26f, 1f),
                new Color(0.42f, 0.36f, 0.30f, 1f),
                new Color(0.18f, 0.16f, 0.14f, 1f),
                new Color(0.90f, 0.80f, 0.40f, 1f),
                new Color(0.95f, 0.85f, 0.45f, 1f),
                new Color(0.55f, 0.42f, 0.25f, 1f),
                new Color(0.40f, 0.30f, 0.18f, 1f)
            );
        };
    }

    private Color shade(Color base, float factor) {
        return new Color(
            clamp(base.r * factor),
            clamp(base.g * factor),
            clamp(base.b * factor),
            base.a
        );
    }

    private float clamp(float v) {
        return Math.max(0f, Math.min(1f, v));
    }

    private void add(String key, Color color) {
        Pixmap px = new Pixmap(TILE_W, TILE_H, Pixmap.Format.RGBA8888);
        px.setColor(color);
        px.fill();

        // 1px darker border so tiles are visually separated
        px.setColor(color.r * 0.6f, color.g * 0.6f, color.b * 0.6f, 1f);
        px.drawRectangle(0, 0, TILE_W, TILE_H);

        Texture tex = new Texture(px);
        tex.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        px.dispose();

        textures.put(key, tex);
        regions.put(key, new TextureRegion(tex));
    }

    @Override
    public TextureRegion getTile(TileType type, int variant) {
        String key = switch (type) {
            case FLOOR -> "floor_" + (variant % FLOOR_VARIANTS);
            case WALL -> "wall_" + (variant % WALL_VARIANTS);
            case BORDER_WALL -> "border_wall";
            case STAIRS_UP -> "stairs_up";
            case STAIRS_DOWN -> "stairs_down";
            case DOOR_OPEN -> "door_open";
            case DOOR_CLOSED -> "door_closed";
        };
        return regions.getOrDefault(key, regions.get("unknown"));
    }

    private void disposeTextures() {
        textures.values().forEach(Texture::dispose);
        textures.clear();
        regions.clear();
    }

    @Override
    public void dispose() {
        disposeTextures();
    }

    private record Palette(
        Color floor,
        Color wall,
        Color borderWall,
        Color stairsUp,
        Color stairsDown,
        Color doorOpen,
        Color doorClosed
    ) {}
}
