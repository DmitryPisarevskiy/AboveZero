package com.dmitry.pisarevskiy.abovezero.weather;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.dmitry.pisarevskiy.abovezero.OpenWeatherMapService;
import com.dmitry.pisarevskiy.abovezero.R;
import com.google.gson.Gson;

import androidx.annotation.Nullable;

public class WeatherJsonProcessService {
    private static final String FAIL_JSON_TAG = "Fail to convert JSON";

    private WeatherSample currentWeather;
    private ForecastWeather forecastWeather;
    private Context context;

    public WeatherJsonProcessService(String currentWeatherJSON, String forecastWeatherJSON, Context context) {
        Gson gson = new Gson();
        this.context = context;
        try {
            currentWeather = gson.fromJson(currentWeatherJSON, WeatherSample.class);
            forecastWeather = gson.fromJson(forecastWeatherJSON,ForecastWeather.class);
        } catch (Exception e) {
            Log.e(FAIL_JSON_TAG, context.getResources().getString(R.string.fail_network), e);
            e.printStackTrace();
        }
    }

    public WeatherSample getCurrentWeather() {
        return currentWeather;
    }

    public ForecastWeather getForecastWeather() {
        return forecastWeather;
    }
}
