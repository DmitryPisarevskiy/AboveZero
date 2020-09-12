package com.dmitry.pisarevskiy.abovezero.weather;

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
}
