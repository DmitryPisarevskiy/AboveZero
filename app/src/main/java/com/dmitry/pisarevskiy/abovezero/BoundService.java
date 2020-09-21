package com.dmitry.pisarevskiy.abovezero;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.dmitry.pisarevskiy.abovezero.weather.ForecastWeather;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherSample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

import androidx.annotation.RequiresApi;

public class BoundService extends Service {
    private static final String HISTORY_WEATHER_URL = "https://api.openweathermap.org/data/2.5/history?id=";
    private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?id=";
    private static final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?id=";
    private static final String API_URL = "&appid=3f371cc26311182846ffe9eeabc50393";
    private static final String FAIL_NETWORK_TAG = "fail network";
    private static final String FAIL_NETWORK = "Не удалось получить данные об этом городе";
    private final IBinder binder = new ServiceBinder();

    private String currentWeatherJSON;
    private String forecastWeatherJSON;

    // Вызывается только 1 раз при создании сервиса
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getCurrentWeatherJSON(String id) throws Exception{
        final URL urlCurrent = new URL(CURRENT_WEATHER_URL + id + API_URL);
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) urlCurrent.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            currentWeatherJSON = in.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return currentWeatherJSON;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getForecastWeatherJSON(String id) throws Exception{
        final URL urlForecast = new URL(FORECAST_WEATHER_URL + id + API_URL);
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) urlForecast.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            forecastWeatherJSON = in.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return forecastWeatherJSON;
    }

    // Класс связи между клиентом и сервисом
    class ServiceBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        String getCurrentWeatherJSON(String id) throws Exception{
            return getService().getCurrentWeatherJSON(id);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        String getForecastWeatherJSON(String id) throws Exception{
            return getService().getForecastWeatherJSON(id);
        }
    }
}
