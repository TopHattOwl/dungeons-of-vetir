package com.tophattowl.dungeonsofvetir.dungeon;

import com.tophattowl.dungeonsofvetir.game.dungeon.GenerationContext;
import com.tophattowl.dungeonsofvetir.game.dungeon.generators.RoomGenerator;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.ResolvedFloor;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.SectionCatalog;
import com.tophattowl.dungeonsofvetir.game.dungeon.section.WorldLayout;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

class RoomGeneratorTest {

    private static GenerationContext ctx(long worldSeed, int floor) {
        WorldLayout layout = new WorldLayout(worldSeed, SectionCatalog.defaultCatalog());
        ResolvedFloor resolved = layout.resolve(floor);
        return new GenerationContext(resolved.seed(), floor, resolved);
    }

    private static Level generate(long worldSeed) {
        return new RoomGenerator().generate(ctx(worldSeed, 7));
    }

    @Test
    void generate_HasExactlyOneUpAndDownStairs() {
        Level level = generate(12345);
        int up = 0;
        int down = 0;
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                if (level.getTile(x, y).type == TileType.STAIRS_UP) up++;
                if (level.getTile(x, y).type == TileType.STAIRS_DOWN) down++;
            }
        }
        assertEquals(1, up);
        assertEquals(1, down);
    }

    @Test
    void generate_StairsAreConnected() {
        Level level = generate(12345);

        int[] up = find(level, TileType.STAIRS_UP);
        int[] down = find(level, TileType.STAIRS_DOWN);
        assertNotNull(up);
        assertNotNull(down);

        assertTrue(reachable(level, up[0], up[1], down[0], down[1]),
            "Stairs down should be reachable from stairs up");
    }

    @Test
    void generate_IsDeterministic() {
        Level a = generate(12345);
        Level b = generate(12345);
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                assertEquals(a.getTile(x, y).type, b.getTile(x, y).type);
            }
        }
    }

    @Test
    void generate_BordersAreWalls() {
        Level level = generate(12345);
        for (int x = 0; x < Level.WIDTH; x++) {
            assertEquals(TileType.WALL, level.getTile(x, 0).type);
            assertEquals(TileType.WALL, level.getTile(x, Level.HEIGHT - 1).type);
        }
        for (int y = 0; y < Level.HEIGHT; y++) {
            assertEquals(TileType.WALL, level.getTile(0, y).type);
            assertEquals(TileType.WALL, level.getTile(Level.WIDTH - 1, y).type);
        }
    }

    private static int[] find(Level level, TileType type) {
        for (int x = 0; x < Level.WIDTH; x++) {
            for (int y = 0; y < Level.HEIGHT; y++) {
                if (level.getTile(x, y).type == type) return new int[]{x, y};
            }
        }
        return null;
    }

    private static boolean reachable(Level level, int sx, int sy, int tx, int ty) {
        boolean[][] visited = new boolean[Level.WIDTH][Level.HEIGHT];
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{sx, sy});
        visited[sx][sy] = true;

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            if (cell[0] == tx && cell[1] == ty) return true;

            for (int[] d : dirs) {
                int nx = cell[0] + d[0];
                int ny = cell[1] + d[1];
                if (nx < 0 || ny < 0 || nx >= Level.WIDTH || ny >= Level.HEIGHT) continue;
                if (visited[nx][ny]) continue;
                if (!level.isWalkable(nx, ny)) continue;
                visited[nx][ny] = true;
                queue.add(new int[]{nx, ny});
            }
        }
        return false;
    }
}
