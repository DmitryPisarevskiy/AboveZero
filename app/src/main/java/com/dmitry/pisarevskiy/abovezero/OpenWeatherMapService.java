package com.dmitry.pisarevskiy.abovezero;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.dmitry.pisarevskiy.abovezero.weather.ForecastWeather;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherSample;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

public class OpenWeatherMapService {
    private static final String HISTORY_WEATHER_URL = "https://api.openweathermap.org/data/2.5/history?id=";
    private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?id=";
    private static final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?id=";
    private static final String API_URL = "&appid=3f371cc26311182846ffe9eeabc50393";
    private static final String FAIL_NETWORK_TAG = "fail network";
    private static final String FAIL_NETWORK = "Не удалось получить данные об этом городе";

    private String currentWeatherJSON;
    private String forecastWeatherJSON;
    private Context context;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public OpenWeatherMapService(String id, final Context context) throws Exception{
        this.context = context;
        final URL urlCurrent = new URL(CURRENT_WEATHER_URL + id + API_URL);
        final URL urlForecast = new URL(FORECAST_WEATHER_URL + id + API_URL);
        final URL urlHistory = new URL(HISTORY_WEATHER_URL + id + API_URL);
        HttpURLConnection urlConnection = null;
        try {

            //Получение текущей погоды
            urlConnection = (HttpURLConnection) urlCurrent.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            currentWeatherJSON = in.lines().collect(Collectors.joining("\n"));

            // Получение прогноза погоды
            urlConnection = (HttpURLConnection) urlForecast.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            forecastWeatherJSON = in.lines().collect(Collectors.joining("\n"));

        } catch (Exception e) {
            Log.e(FAIL_NETWORK_TAG, context.getResources().getString(R.string.fail_network), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    public String getCurrentWeatherJSON() {
        return currentWeatherJSON;
    }

    public String getForecastWeatherJSON() {
        return forecastWeatherJSON;
    }

}
