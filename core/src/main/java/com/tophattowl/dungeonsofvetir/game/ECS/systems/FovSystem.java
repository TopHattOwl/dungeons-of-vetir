package com.tophattowl.dungeonsofvetir.game.ECS.systems;

import com.badlogic.gdx.utils.Disposable;
import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.actors.components.FovComponent;
import com.tophattowl.dungeonsofvetir.game.actors.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.event.EventSubscriptions;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityAddedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.EntityMovedEvent;
import com.tophattowl.dungeonsofvetir.game.event.events.LevelChangedEvent;
import com.tophattowl.dungeonsofvetir.game.world.Capability;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;

/**
 * Recursive shadowcasting FOV, recomputed per entity.
 * <p>
 * FOV depends only on the entity's own position and the level's opacity, so it is
 * refreshed when the entity is added or moves, and for everyone on a level change.
 * Every entity's FOV is therefore never stale for its own decisions (AI included),
 * not just the player's.
 */
public class FovSystem implements Disposable {

    private final GameWorld gameWorld;
    private final EventSubscriptions eventSubs = new EventSubscriptions();

    public FovSystem(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        eventSubs.on(EntityAddedEvent.class, e -> update(e.entity()));
        eventSubs.on(EntityMovedEvent.class, e -> update(e.entity()));
        eventSubs.on(LevelChangedEvent.class, e -> updateAll());
    }

    /**
     * Recomputes FOV for a single entity, if it has position and vision.
     */
    public void update(Entity entity) {
        if (entity == null) return;

        PositionComponent pos = entity.getComponent(PositionComponent.class);
        FovComponent fov = entity.getComponent(FovComponent.class);
        if (pos == null || fov == null) return;

        // e.g. the overworld reveals the whole map: no per-entity FOV
        if (!gameWorld.constraints().can(entity, Capability.VISION)) return;

        Level level = gameWorld.getCurrentLevel();
        if (level == null) return;

        computeFov(level, pos.getX(), pos.getY(), fov);
    }

    /**
     * Recomputes FOV for every entity that has position and vision (level change).
     */
    public void updateAll() {
        if (gameWorld.getCurrentLevel() == null) return;

        for (Entity entity : gameWorld.query(PositionComponent.class, FovComponent.class)) {
            update(entity);
        }
    }

    private void computeFov(Level level, int originX, int originY, FovComponent fov) {
        fov.clearVisible();

        // origin is always visible
        markVisible(fov, originX, originY);

        // Cast into all 8 octants
        for (int octant = 0; octant < 8; octant++) {
            castOctant(level, fov, originX, originY, octant, 1, 0.0f, 1.0f);
        }
    }

    private void markVisible(FovComponent fov, int x, int y) {
        if (x < 0 || y < 0 || x >= fov.visibleTiles.length || y >= fov.visibleTiles[0].length)
            return;
        fov.visibleTiles[x][y] = true;
        fov.exploredTiles[x][y] = true;
    }

    private void castOctant(Level level, FovComponent fov,
                            int originX, int originY,
                            int octant, int row,
                            float startSlope, float endSlope) {
        if (startSlope >= endSlope) return;
        if (row > fov.visionRadius) return;

        boolean prevBlocked = false;
        float newStart = startSlope;

        for (int col = 0; col <= row; col++) {
            // slopes for left and right edges of this tile
            float leftSlope  = (col - 0.5f) / (row + 0.5f);
            float rightSlope = (col + 0.5f) / (row - 0.5f); // tries with row - 0.5 and row + 0.5 both work the same

            // skip if outside current sector
            if (leftSlope > endSlope || rightSlope < startSlope) continue;

            // transform col/row in octant space to world coordinates
            int wx = transformX(originX, row, col, octant);
            int wy = transformY(originY, row, col, octant);

            if (!level.isInBounds(wx, wy)) continue;

            // circular distance check
            if (col * col + row * row <= fov.visionRadius * fov.visionRadius) {
                markVisible(fov, wx, wy);
            }

            boolean blocked = !level.isTransparent(wx, wy);

            if (prevBlocked) {
                if (blocked) {
                    newStart = rightSlope;
                } else {
                    // transition wall to floor -> restore start slope
                    prevBlocked = false;
                    startSlope = newStart;
                }
            } else {
                if (blocked) {
                    // transition floor to wall -> recurse for the beam above this wall
                    castOctant(level, fov, originX, originY, octant,
                        row + 1, startSlope, leftSlope);
                    prevBlocked = true;
                    newStart = rightSlope;
                }
            }
        }

        // row didn't end on a wall -> continue to next row
        if (!prevBlocked) {
            castOctant(level, fov, originX, originY, octant,
                row + 1, startSlope, endSlope);
        }
    }

    private int transformX(int ox, int row, int col, int octant) {
        return switch (octant) {
            case 0 ->  ox + col;  // N
            case 1 ->  ox + row;  // NE
            case 2 ->  ox + row;  // E
            case 3 ->  ox + col;  // SE
            case 4 ->  ox - col;  // S
            case 5 ->  ox - row;  // SW
            case 6 ->  ox - row;  // W
            case 7 ->  ox - col;  // NW
            default -> ox;
        };
    }

    private int transformY(int oy, int row, int col, int octant) {
        return switch (octant) {
            case 0 ->  oy - row;  // N
            case 1 ->  oy - col;  // NE
            case 2 ->  oy + col;  // E
            case 3 ->  oy + row;  // SE
            case 4 ->  oy + row;  // S
            case 5 ->  oy + col;  // SW
            case 6 ->  oy - col;  // W
            case 7 ->  oy - row;  // NW
            default -> oy;
        };
    }

    @Override
    public void dispose() {
        eventSubs.unsubscribeAll();
    }
}
