package com.tophattowl.dungeonsofvetir.game.actors.components;

import com.tophattowl.dungeonsofvetir.game.ECS.Component;

public class TimeValueComponent implements Component {
    public float baseSpeed;
    public int timeValueSum;

    public TimeValueComponent(float baseSpeed, int timeValueSum) {
        this.baseSpeed = baseSpeed;
        this.timeValueSum = timeValueSum;
    }

    public TimeValueComponent() {
        this(1.0f, 0);
    }

    public TimeValueComponent(int timeValueSum) {
        this(1.0f, timeValueSum);
    }

    public TimeValueComponent(float baseSpeed) {
        this(baseSpeed, 0);
    }

    public void addTime(int time) {
        timeValueSum += time;
    }

    public void resetTimeValueSum() {
        timeValueSum = 0;
    }
}
