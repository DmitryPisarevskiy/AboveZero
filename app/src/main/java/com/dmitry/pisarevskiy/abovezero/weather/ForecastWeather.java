package com.dmitry.pisarevskiy.abovezero.weather;

import android.content.res.Resources;

import com.dmitry.pisarevskiy.abovezero.MainActivity;
import com.dmitry.pisarevskiy.abovezero.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ForecastWeather {
    private WeatherSample[] list;
    private City city;
    private String request;

    public void setRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public WeatherSample[] getList() {
        return list;
    }

    public City getCity() {
        return city;
    }

    public void setList(WeatherSample[] list) {
        this.list = list;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public float[] getTemps(int numOfData) {
        float[] arr=new float[numOfData];
        for (int i = 0; i < numOfData; i++) {
            arr[i] = getList()[i].getMain().getTemp();
        }
        return arr;
    }

    public float[] getPressures(int numOfData) {
        float[] arr=new float[numOfData];
        for (int i = 0; i < numOfData; i++) {
            arr[i] = getList()[i].getMain().getPressure();
        }
        return arr;
    }

    public float[] getWinds(int numOfData) {
        float[] arr=new float[numOfData];
        for (int i = 0; i < numOfData; i++) {
            arr[i] = getList()[i].getWind().getSpeed();
        }
        return arr;
    }

    public String[] getTimes(int numOfData) {
        String[] arr = new String[numOfData];
        for (int i = 0; i < numOfData; i++) {
            Date date = new java.util.Date((getList()[i].getDt() + city.getTimezone())*1000L);
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
            arr[i]=sdf.format(date);
        }
        return arr;
    }

    public int[] getImages(int numOfData) {
        int[] arr = new int[numOfData];
        for (int i = 0; i < numOfData; i++) {
            arr[i]=getList()[i].getImage();
        }
        return arr;
    }
}
