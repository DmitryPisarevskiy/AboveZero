package com.dmitry.pisarevskiy.abovezero.weather;

import com.dmitry.pisarevskiy.abovezero.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current extends Hourly{
    @SerializedName("sunrise")
    @Expose
    private long sunrise;

    @SerializedName("sunset")
    @Expose
    private long sunsent;

    public long getSunrise() {
        return sunrise;
    }

    public long getSunsent() {
        return sunsent;
    }
}
