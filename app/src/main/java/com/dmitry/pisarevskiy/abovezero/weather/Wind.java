package com.dmitry.pisarevskiy.abovezero.weather;

public class Wind {
    private int speed;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }

    private int deg;
}
