package com.dmitry.pisarevskiy.abovezero.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherOneCall {
    @SerializedName("timezone_offset")
    @Expose
    private int timeZone;

    @SerializedName("current")
    @Expose
    private Current current;

    @SerializedName("hourly")
    @Expose
    private Hourly[] hourly;

    @SerializedName("daily")
    @Expose
    private Daily[] daily;

    public int getTimeZone() {
        return timeZone;
    }

    public Current getCurrent() {
        return current;
    }

    public Hourly[] getHourly() {
        return hourly;
    }

    public Daily[] getDaily() {
        return daily;
    }

    public String[] getHours() {
        String[] arr = new String[hourly.length];
        for (int i = 0; i < arr.length; i++) {
            Date date = new java.util.Date((hourly[i].getDt() + timeZone)*1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", Locale.US);
            arr[i]=sdf.format(date);
        }
        return arr;
    }

    public String[] getDays() {
        String[] arr = new String[daily.length];
        for (int i = 0; i < arr.length; i++) {
            Date date = new java.util.Date((daily[i].getDt() + timeZone)*1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd", Locale.US);
            arr[i]=sdf.format(date);
        }
        return arr;
    }


    public int[] getHoursImages() {
        int[] arr = new int[hourly.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i]=hourly[i].getImage();
        }
        return arr;
    }

    public int[] getDaysImages() {
        int[] arr = new int[daily.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i]=daily[i].getImage();
        }
        return arr;
    }

    public float[] getHoursTemps() {
        float[] arr=new float[hourly.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = hourly[i].getTemp();
        }
        return arr;
    }

    public float[] getDaysTemps() {
        float[] arr=new float[daily.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = daily[i].getTemp().getDay();
        }
        return arr;
    }

    public float[] getHoursPressures() {
        float[] arr=new float[hourly.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = hourly[i].getPressure();
        }
        return arr;
    }


    public float[] getDaysPressures() {
        float[] arr=new float[daily.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = daily[i].getPressure();
        }
        return arr;
    }

    public float[] getHoursWinds() {
        float[] arr=new float[hourly.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = hourly[i].getWindSpeed();
        }
        return arr;
    }

    public float[] getDaysWinds() {
        float[] arr=new float[daily.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = daily[i].getWindSpeed();
        }
        return arr;
    }
}
