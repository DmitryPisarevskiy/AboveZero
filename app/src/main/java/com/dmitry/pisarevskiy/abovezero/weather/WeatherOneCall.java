package com.dmitry.pisarevskiy.abovezero.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherOneCall {
    @SerializedName("current")
    @Expose
    private Current current;

    public Current getCurrent() {
        return current;
    }
}
