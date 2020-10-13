package com.dmitry.pisarevskiy.abovezero.weather;

import com.dmitry.pisarevskiy.abovezero.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current extends Hourly{
    @SerializedName("sunrise")
    @Expose
    private int sunrise;

    @SerializedName("sunset")
    @Expose
    private int sunset;

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }
}
