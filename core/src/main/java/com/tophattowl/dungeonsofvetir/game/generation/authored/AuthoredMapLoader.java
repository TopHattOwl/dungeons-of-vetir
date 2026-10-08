package com.tophattowl.dungeonsofvetir.game.generation.authored;

import com.tophattowl.dungeonsofvetir.game.world.DeclaredExit;
import com.tophattowl.dungeonsofvetir.game.world.Level;
import com.tophattowl.dungeonsofvetir.game.world.PlaceKind;
import com.tophattowl.dungeonsofvetir.game.world.Point;
import com.tophattowl.dungeonsofvetir.game.world.TileType;
import com.tophattowl.dungeonsofvetir.game.world.TransitionKind;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses the text map format:
 * <pre>
 * name: Cellar
 * width: 10
 * height: 6
 * tiles:
 * ##########
 * #........#
 * #...+....#
 * #........#
 * #........#
 * ##########
 * exits:
 * 4 2 DOOR SIDE_ROOM cellar
 * </pre>
 * Tile chars: '#' wall, '.' floor, '&gt;' stairs down, '&lt;' stairs up, '+' open door, '=' closed door <br>
 * Exit line: {@code x y TRANSITION_KIND TARGET_KIND targetKey} <br>
 * Lines starting with {@code //} and blank lines are ignored
 */
public final class AuthoredMapLoader {

    private AuthoredMapLoader() {}

    public static AuthoredMap parse(String text, int floorNumber) {
        String name = "";
        int width = -1;
        int height = -1;
        List<String> rows = new ArrayList<>();
        List<DeclaredExit> exits = new ArrayList<>();

        int state = 0; // 0 = header, 1 = tiles, 2 = exits

        for (String raw : text.split("\n")) {
            String line = raw.strip();
            if (line.isEmpty() || line.startsWith("//")) continue;

            if (line.equals("tiles:")) {
                state = 1;
                continue;
            }
            if (line.equals("exits:")) {
                state = 2;
                continue;
            }

            switch (state) {
                case 0 -> {
                    int colon = line.indexOf(':');
                    if (colon < 0) continue;
                    String key = line.substring(0, colon).strip().toLowerCase();
                    String value = line.substring(colon + 1).strip();
                    switch (key) {
                        case "name" -> name = value;
                        case "width" -> width = Integer.parseInt(value);
                        case "height" -> height = Integer.parseInt(value);
                        default -> { }
                    }
                }
                case 1 -> rows.add(line);
                default -> exits.add(parseExit(line));
            }
        }

        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Authored map is missing width/height");
        }

        Level level = new Level(floorNumber, width, height);
        for (int y = 0; y < height; y++) {
            String row = rows.get(y);
            for (int x = 0; x < width; x++) {
                level.setTile(x, y, charToType(row.charAt(x)));
            }
        }
        level.setDeclaredExits(exits);

        return new AuthoredMap(name, level, exits);
    }

    private static DeclaredExit parseExit(String line) {
        String[] parts = line.split("\\s+");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        TransitionKind kind = TransitionKind.valueOf(parts[2].toUpperCase());
        PlaceKind targetKind = PlaceKind.valueOf(parts[3].toUpperCase());
        String targetKey = parts[4];
        return new DeclaredExit(new Point(x, y), kind, targetKind, targetKey);
    }

    private static TileType charToType(char c) {
        return switch (c) {
            case '#' -> TileType.WALL;
            case '.' -> TileType.FLOOR;
            case '>' -> TileType.STAIRS_DOWN;
            case '<' -> TileType.STAIRS_UP;
            case '+' -> TileType.DOOR_OPEN;
            case '=' -> TileType.DOOR_CLOSED;
            default -> throw new IllegalArgumentException("Unknown map char: '" + c + "'");
        };
    }
}
