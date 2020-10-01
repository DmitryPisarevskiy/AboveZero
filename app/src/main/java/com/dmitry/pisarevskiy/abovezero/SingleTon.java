package com.dmitry.pisarevskiy.abovezero;

import com.dmitry.pisarevskiy.abovezero.weather.Request;

import java.util.ArrayList;

public final class SingleTon {
    private static SingleTon instance = null;
    private static final Object syncObj = new Object();
    private boolean showWindSpeed;
    private boolean showPressure;
    private boolean nightMode;
    private int pressureUnit;
    private int windSpeedUnit;
    private ArrayList<Request> history;

    public ArrayList<Request> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Request> history) {
        this.history = history;
    }

    public void setShowPressure(boolean showPressure) {
        this.showPressure = showPressure;
    }

    public boolean isShowPressure() {
        return showPressure;
    }

    public void setNightMode(boolean nightMode) {
        this.nightMode = nightMode;
    }

    public void setPressureUnit(int pressureUnit) {
        this.pressureUnit = pressureUnit;
    }

    public void setWindSpeedUnit(int windSpeedUnit) {
        this.windSpeedUnit = windSpeedUnit;
    }

    public boolean isNightMode() {
        return nightMode;
    }

    public int getPressureUnit() {
        return pressureUnit;
    }

    public int getWindSpeedUnit() {
        return windSpeedUnit;
    }

    private SingleTon() {
        showWindSpeed = true;
        showPressure = true;
        nightMode = false;
        pressureUnit = 0;
        windSpeedUnit = 0;
        history = new ArrayList<>();
    }

    public void setShowWindSpeed(boolean showWindSpeed) {
        this.showWindSpeed = showWindSpeed;
    }

    public boolean isShowWindSpeed() {
        return showWindSpeed;
    }

    public static SingleTon getInstance() {
        synchronized (syncObj) {
            if (instance == null) {
                instance = new SingleTon();
            }
            return instance;
        }
    }
}
