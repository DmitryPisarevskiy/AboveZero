package com.dmitry.pisarevskiy.abovezero.weather;

import com.dmitry.pisarevskiy.abovezero.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherSample {
    @SerializedName("weather")
    @Expose
    private Weather[] weather;

    @SerializedName("main")
    @Expose
    private Main main;

    @SerializedName("wind")
    @Expose
    private Wind wind;

    @SerializedName("clouds")
    @Expose
    private Clouds clouds;

    @SerializedName("dt_txt")
    @Expose
    private String dt_txt;

    @SerializedName("dt")
    @Expose
    private int dt;

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public int getImage() {
        int img;
        switch (getWeather()[0].getMain()){
            case "Drizzle":
                img = R.drawable.week_rain;
                break;
            case "Rain":
                if (getWeather()[0].getDescription().equals("light rain") ||
                        getWeather()[0].getDescription().equals("moderate rain")) {
                    img =R.drawable.week_rain;
                } else {
                    img =R.drawable.strong_rain;
                }
                break;
            case "Clouds":
                if (getWeather()[0].getDescription().equals("overcast clouds")) {
                    img=R.drawable.cloudly;
                } else {
                    img=R.drawable.week_cloudly;
                }
                break;
            case "Clear":
                img=R.drawable.sunny;
                break;
            default:
                img = R.drawable.cloudly;
        }
        return img;
    }

}
