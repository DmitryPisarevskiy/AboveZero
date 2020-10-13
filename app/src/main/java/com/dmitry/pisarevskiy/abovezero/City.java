package com.dmitry.pisarevskiy.abovezero;

public class City {
    private int id;
    private String name;
    private float lon;
    private float lat;

    public City(int id, String name, float lon, float lat) {
        this.id = id;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }
}
