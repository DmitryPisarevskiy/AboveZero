package com.dmitry.pisarevskiy.abovezero.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Temp {
    @SerializedName("day")
    @Expose
    private float day;

    @SerializedName("night")
    @Expose
    private float night;

    public float getDay() {
        return day;
    }

    public float getNight() {
        return night;
    }
}
