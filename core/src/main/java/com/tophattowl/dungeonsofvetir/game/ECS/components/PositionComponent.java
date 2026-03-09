package com.tophattowl.dungeonsofvetir.game.ECS.components;

import com.tophattowl.dungeonsofvetir.game.world.Point;

public class PositionComponent implements Component{
    public Point position;

    public PositionComponent(int x, int y) {
        this. position = new Point(x,y);
    }

    public PositionComponent(Point position) {
        this.position = position;
    }

    public void set(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    public void set(Point position) {
        this.position = position;
    }

    public void setX(int x) {
        position.setX(x);
    }
    public void setY(int y) {
        position.setY(y);
    }

    public int getX() {
        return position.x;
    }
    public int getY() {
        return position.y;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "Position(" + position.x + ", " + position.y + ")";
    }
}
