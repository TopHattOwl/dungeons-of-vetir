package com.tophattowl.dungeonsofvetir.game.ECS.systems;

import com.tophattowl.dungeonsofvetir.game.ECS.Entity;
import com.tophattowl.dungeonsofvetir.game.ECS.components.FovComponent;
import com.tophattowl.dungeonsofvetir.game.ECS.components.PositionComponent;
import com.tophattowl.dungeonsofvetir.game.world.GameWorld;
import com.tophattowl.dungeonsofvetir.game.world.Level;

import java.util.List;

/**
 * Recursive shadowcasting FOV
 * Runs every turn for every entity with Position and Fov components
 * Updates visibleTiles and exploredTiles
 */
public class FovSystem implements GameSystem{

    @Override
    public void process(GameWorld gameWorld) {
        Level level = gameWorld.getCurrentLevel();

        if (level == null) return;

        List<Entity> entities = gameWorld.querry(PositionComponent.class, FovComponent.class);
        if (entities.isEmpty()) return;

        for (Entity entity : entities) {
            PositionComponent pos = entity.getComponent(PositionComponent.class);
            FovComponent fov = entity.getComponent(FovComponent.class);
            computeFov(level, pos.getX(), pos.getY(), fov);
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
        if (row > fov.radius) return;

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
//            float dist = (float) Math.sqrt(col * col + row * row);
//            if (dist <= fov.radius) {
//                markVisible(fov, wx, wy);
//            }
            if (col * col + row * row <= fov.radius * fov.radius) {
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


}
