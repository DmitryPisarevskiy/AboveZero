package com.dmitry.pisarevskiy.abovezero.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("sunrise")
    @Expose
    private long sunrise;

    @SerializedName("timezone")
    @Expose
    private long timezone;

    @SerializedName("sunset")
    @Expose
    private long sunset;

    public void setTimezone(long timezone) {
        this.timezone = timezone;
    }

    public long getTimezone() {
        return timezone;
    }

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
