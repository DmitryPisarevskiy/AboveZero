package com.dmitry.pisarevskiy.abovezero.weather;

public class City {
    private int id;
    private String name;
    private long sunrise;
    private long sunset;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getSunrise() {
        return sunrise;
    }

    public long getSunset() {
        return sunset;
    }
}
