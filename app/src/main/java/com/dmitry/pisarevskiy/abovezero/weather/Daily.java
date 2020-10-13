package com.dmitry.pisarevskiy.abovezero.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Daily extends Sample {
    @SerializedName("temp")
    @Expose
    private Temp temp;

    @SerializedName("feels_like")
    @Expose
    private Temp feelsLike;

    public Temp getTemp() {
        return temp;
    }

    public Temp getFeelsLike() {
        return feelsLike;
    }
}
