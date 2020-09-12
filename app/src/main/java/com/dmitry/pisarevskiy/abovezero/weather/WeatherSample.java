package com.dmitry.pisarevskiy.abovezero.weather;

public class WeatherSample {
    private Weather[] weather;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private String dt_txt;

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public String getDt_txt() {
        return dt_txt;
    }
//    private Sys sys;
//    private String name;
//    private int id;

//    public void setId(int id) {
//        this.id = id;
//    }

//    public int getId() {
//        return id;
//    }

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

//    public void setSys(Sys sys) {
//        this.sys = sys;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

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

//    public Sys getSys() {
//        return sys;
//    }
//
//    public String getName() {
//        return name;
//    }
}
