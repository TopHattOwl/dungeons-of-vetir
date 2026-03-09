package com.tophattowl.dungeonsofvetir.game.ECS.components;

public class TimeValueComponent implements Component {
    public float baseSpeed;
    public int timeValueSum;

    public TimeValueComponent(float baseSpeed, int timeValueSum) {
        this.baseSpeed = baseSpeed;
        this.timeValueSum = timeValueSum;
    }

    public TimeValueComponent() {
        baseSpeed = 1.0f;
        timeValueSum = 0;
    }

    public TimeValueComponent(int timeValueSum) {
        this.timeValueSum = timeValueSum;
    }

    public void addTime(int time) {
        timeValueSum += time;
    }

    public void resetTimeValueSum() {
        timeValueSum = 0;
    }
}
