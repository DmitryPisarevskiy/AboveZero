package com.dmitry.pisarevskiy.abovezero;

import com.dmitry.pisarevskiy.abovezero.weather.Current;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherOneCall;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherRequest;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherSample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/forecast")
    Call<WeatherRequest> loadForecastWeather(@Query("id") String cityID, @Query("appid") String keyApi);

    @GET("data/2.5/weather")
    Call<WeatherSample> loadCurrentWeather(@Query("id") String cityID, @Query("appid") String keyApi);

    @GET("data/2.5/onecall")
    Call<WeatherOneCall> loadOneCallWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String keyApi);
}