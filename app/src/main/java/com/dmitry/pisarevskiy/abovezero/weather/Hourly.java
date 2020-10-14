package com.dmitry.pisarevskiy.abovezero.weather;

import com.dmitry.pisarevskiy.abovezero.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hourly extends Sample{
    @SerializedName("temp")
    @Expose
    private float temp;

    @SerializedName("feels_like")
    @Expose
    private float feelsLike;

    public float getTemp() {
        return temp;
    }

    public float getFeelsLike() {
        return feelsLike;
    }
}
