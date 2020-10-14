package com.dmitry.pisarevskiy.abovezero.weather;

public class Wind {
    private float speed;

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public float getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }

    private int deg;
}
