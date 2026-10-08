package com.tophattowl.dungeonsofvetir.game.generation.generators;

import com.badlogic.gdx.Gdx;
import com.tophattowl.dungeonsofvetir.game.generation.GenerationContext;
import com.tophattowl.dungeonsofvetir.game.generation.LevelGenerator;
import com.tophattowl.dungeonsofvetir.game.generation.authored.AuthoredMap;
import com.tophattowl.dungeonsofvetir.game.generation.authored.AuthoredMapLoader;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

/**
 * Loads a premade zone from {@code assets/maps/<zoneKey>.txt}. Used for the
 * overworld, settlements, dungeon surfaces and nested side rooms.
 */
public class AuthoredLevelGenerator implements LevelGenerator {

    @Override
    public Level generate(GenerationContext ctx) {
        // Headless/tests have no Gdx file backend; fall back to a minimal valid zone
        // (runtime always has Gdx). Keeps GameWorld constructible without a display.
        if (Gdx.files == null) {
            return fallback(ctx);
        }

        String path = "maps/" + ctx.zoneKey() + ".txt";
        String text = Gdx.files.internal(path).readString();
        AuthoredMap map = AuthoredMapLoader.parse(text, ctx.floorNumber());
        return map.level();
    }

    private Level fallback(GenerationContext ctx) {
        Level level = new Level(ctx.floorNumber(), ctx.width(), ctx.height());
        for (int x = 1; x < level.getWidth() - 1; x++) {
            for (int y = 1; y < level.getHeight() - 1; y++) {
                level.setTile(x, y, TileType.FLOOR);
            }
        }
        level.setTile(level.getWidth() / 2, level.getHeight() / 2, TileType.STAIRS_DOWN);
        return level;
    }
}
