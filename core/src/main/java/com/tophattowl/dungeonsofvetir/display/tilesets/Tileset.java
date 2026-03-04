package com.tophattowl.dungeonsofvetir.display.tilesets;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tophattowl.dungeonsofvetir.game.world.TileType;


public interface Tileset {
    int TILE_W = 16;
    int TILE_H = 24;

    /**
     * Get the texture region for a map tile
     * @param type    the tile type
     * @param variant which visual variant (for tiles with multiple looks)
     */
    TextureRegion getTile(TileType type, int variant);

    /**
     * Get the texture region for an entity sprite
     * @param spriteId the ID stored in RenderableComponent
     */
    TextureRegion getSprite(String spriteId);

    void dispose();
}
