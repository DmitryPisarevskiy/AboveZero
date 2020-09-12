package com.dmitry.pisarevskiy.abovezero.weather;

import android.content.res.Resources;

import com.dmitry.pisarevskiy.abovezero.MainActivity;
import com.dmitry.pisarevskiy.abovezero.R;

public class ForecastWeather {
    private WeatherSample[] list;
    private City city;

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
            arr[i]=getList()[i].getDt_txt().substring(12,16);
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
