package com.dmitry.pisarevskiy.abovezero;

public final class SingleTon {
    private static SingleTon instance = null;
    private static final Object syncObj = new Object();

    private boolean showWindSpeed;
    private boolean showPressure;
    private boolean nightMode;
    private int pressureUnit;
    private int windSpeedUnit;

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
