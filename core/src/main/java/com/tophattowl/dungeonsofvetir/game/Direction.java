package com.tophattowl.dungeonsofvetir.game;

import com.badlogic.gdx.Input;

public enum Direction {
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1),
    STAY(0, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }
    public int getDy() {
        return dy;
    }

    public Direction getOpposite() {
        return fromDxDy(-dx, -dy);
    }

    public Direction fromDxDy(int dx, int dy) {
        for (Direction dir : values()) {
            if (dir.getDx() == dx && dir.getDy() == dy) {
                return dir;
            }
        }
        return STAY;
    }

    public static Direction fromKeyCode(int keyCode) {
        return switch (keyCode) {
            case Input.Keys.NUMPAD_8 -> NORTH;
            case Input.Keys.NUMPAD_9 -> NORTH_EAST;
            case Input.Keys.NUMPAD_6 -> EAST;
            case Input.Keys.NUMPAD_3 -> SOUTH_EAST;
            case Input.Keys.NUMPAD_2 -> SOUTH;
            case Input.Keys.NUMPAD_1 -> SOUTH_WEST;
            case Input.Keys.NUMPAD_4 -> WEST;
            case Input.Keys.NUMPAD_7 -> NORTH_WEST;
            case Input.Keys.NUMPAD_5 -> STAY;
            default -> null;
        };
    }


    @Override
    public String toString() {
        return "(Direction Enum) dx: " + dx + ", dy: " + dy + ", direction: " + name();
    }
}
