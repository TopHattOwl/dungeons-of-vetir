package com.tophattowl.dungeonsofvetir.game.ECS.components;

public class EnergyComponent implements Component {
    public float baseSpeed;
    public int energySum;

    public EnergyComponent(float baseSpeed, int energySum) {
        this.baseSpeed = baseSpeed;
        this.energySum = energySum;
    }

    public EnergyComponent() {
        baseSpeed = 1.0f;
        energySum = 0;
    }

    public EnergyComponent(int energySum) {
        this.energySum = energySum;
    }

    public void addEnergy(int energy) {
        energySum += energy;
    }

    public void resetEnergy() {
        energySum = 0;
    }
}
