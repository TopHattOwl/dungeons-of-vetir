package com.tophattowl.dungeonsofvetir.game.dungeon.generators;

import com.tophattowl.dungeonsofvetir.game.dungeon.GenerationContext;
import com.tophattowl.dungeonsofvetir.game.dungeon.LevelGenerator;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.TileType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates a fortress/ruins layout: non-overlapping rectangular rooms connected
 * by orthogonal L-shaped corridors. Openings where a corridor meets a room wall
 * are marked as doors
 */
public class RoomGenerator implements LevelGenerator {

    private final RoomParams params;

    public RoomGenerator() {
        this(RoomParams.DEFAULT);
    }

    public RoomGenerator(RoomParams params) {
        this.params = params;
    }

    @Override
    public Level generate(GenerationContext ctx) {
        Level level = new Level(ctx.floorNumber());
        Random rng = new Random(ctx.seed());

        List<Room> rooms = placeRooms(rng);
        if (rooms.size() < 2) {
            return new CaveGenerator().generate(ctx);
        }

        for (Room room : rooms) {
            carveRoom(level, room, rng);
        }

        for (int i = 1; i < rooms.size(); i++) {
            connect(level, rooms.get(i - 1), rooms.get(i), rng);
        }

        placeDoors(level);

        Room first = rooms.get(0);
        Room last = rooms.get(rooms.size() - 1);
        level.setTile(first.centerX(), first.centerY(), TileType.STAIRS_UP);
        level.setTile(last.centerX(), last.centerY(), TileType.STAIRS_DOWN);

        return level;
    }

    private List<Room> placeRooms(Random rng) {
        List<Room> rooms = new ArrayList<>();
        int attempts = 0;

        while (rooms.size() < params.roomCount() && attempts < params.maxPlacementAttempts()) {
            attempts++;

            int w = randomSize(rng);
            int h = randomSize(rng);
            int x = 1 + rng.nextInt(Level.WIDTH - 2 - w);
            int y = 1 + rng.nextInt(Level.HEIGHT - 2 - h);
            Room candidate = new Room(x, y, w, h);

            if (overlapsAny(candidate, rooms)) continue;
            rooms.add(candidate);
        }

        return rooms;
    }

    private int randomSize(Random rng) {
        return params.minRoomSize() + rng.nextInt(params.maxRoomSize() - params.minRoomSize() + 1);
    }

    private boolean overlapsAny(Room candidate, List<Room> rooms) {
        // margin of 1 keeps at least one wall between rooms
        for (Room room : rooms) {
            if (candidate.x() - 1 < room.x() + room.w()
                && candidate.x() + candidate.w() + 1 > room.x()
                && candidate.y() - 1 < room.y() + room.h()
                && candidate.y() + candidate.h() + 1 > room.y()) {
                return true;
            }
        }
        return false;
    }

    private void carveRoom(Level level, Room room, Random rng) {
        for (int x = room.x(); x < room.x() + room.w(); x++) {
            for (int y = room.y(); y < room.y() + room.h(); y++) {
                level.setTile(x, y, TileType.FLOOR, rng.nextInt(params.floorVariants()));
            }
        }
    }

    private void connect(Level level, Room from, Room to, Random rng) {
        int x1 = from.centerX();
        int y1 = from.centerY();
        int x2 = to.centerX();
        int y2 = to.centerY();

        if (rng.nextBoolean()) {
            carveHorizontal(level, x1, x2, y1);
            carveVertical(level, y1, y2, x2);
        } else {
            carveVertical(level, y1, y2, x1);
            carveHorizontal(level, x1, x2, y2);
        }
    }

    private void carveHorizontal(Level level, int x1, int x2, int y) {
        int from = Math.min(x1, x2);
        int to = Math.max(x1, x2);
        for (int x = from; x <= to; x++) {
            carveCorridorTile(level, x, y);
        }
    }

    private void carveVertical(Level level, int y1, int y2, int x) {
        int from = Math.min(y1, y2);
        int to = Math.max(y1, y2);
        for (int y = from; y <= to; y++) {
            carveCorridorTile(level, x, y);
        }
    }

    private void carveCorridorTile(Level level, int x, int y) {
        if (level.getTile(x, y).type == TileType.WALL) {
            level.setTile(x, y, TileType.FLOOR, 0);
        }
    }

    /**
     * A wall tile with walkable tiles on both sides (horizontally or vertically)
     * is an opening between a room and a corridor -> make it a door.
     */
    private void placeDoors(Level level) {
        for (int x = 1; x < Level.WIDTH - 1; x++) {
            for (int y = 1; y < Level.HEIGHT - 1; y++) {
                if (level.getTile(x, y).type != TileType.WALL) continue;

                boolean horizontal = isWalkable(level, x - 1, y) && isWalkable(level, x + 1, y);
                boolean vertical = isWalkable(level, x, y - 1) && isWalkable(level, x, y + 1);

                if (horizontal ^ vertical) {
                    level.setTile(x, y, TileType.DOOR_OPEN);
                }
            }
        }
    }

    private boolean isWalkable(Level level, int x, int y) {
        TileType type = level.getTile(x, y).type;
        return type != TileType.WALL && type != TileType.BORDER_WALL;
    }

    private record Room(int x, int y, int w, int h) {
        int centerX() { return x + w / 2; }
        int centerY() { return y + h / 2; }
    }
}
