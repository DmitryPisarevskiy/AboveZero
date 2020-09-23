package com.dmitry.pisarevskiy.abovezero;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.dmitry.pisarevskiy.abovezero.weather.ForecastWeather;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherSample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class NetRequestService extends IntentService {
    private static final String ONECALL_WEATHER_URL = "https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&appid=";
    private static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?id=";
    private static final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?id=";
    private static final String API_URL = "&appid=3f371cc26311182846ffe9eeabc50393";
    private static final String FAIL_NETWORK_TAG = "fail network";
    private static final String FAIL_NETWORK = "Не удалось получить данные об этом городе";
    private static final String EXTRA_ID = "id";
    static final String EXTRA_CURRENT_WEATHER_JSON = "current JSON";
    static final String EXTRA_FORECAST_WEATHER_JSON = "forecast JSON";


    public NetRequestService() {
        super("NetRequestService");
    }


    public static void startNetRequestService(Context context, String id) {
        Intent intent = new Intent(context, NetRequestService.class);
        intent.putExtra(EXTRA_ID, id);
        context.startService(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String id = intent.getStringExtra(EXTRA_ID);
        String currentWeatherJSON="";
        String forecastWeatherJSON="";
        try {
            final URL urlCurrent = new URL(CURRENT_WEATHER_URL + id + API_URL);
            final URL urlForecast = new URL(FORECAST_WEATHER_URL + id + API_URL);
            final URL urlOneCall = new URL(ONECALL_WEATHER_URL + "3f371cc26311182846ffe9eeabc50393");
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
//                Log.e(FAIL_NETWORK_TAG, context.getResources().getString(R.string.fail_network), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        sendBroadcast(currentWeatherJSON, forecastWeatherJSON);
    }


    private void sendBroadcast (String currentWeatherJSON, String forecastWeatherJSON) {
        Intent broadcastIntent = new Intent(MainActivity.BROADCAST_DATA_LOADED);
        broadcastIntent.putExtra(EXTRA_CURRENT_WEATHER_JSON, currentWeatherJSON);
        broadcastIntent.putExtra(EXTRA_FORECAST_WEATHER_JSON, forecastWeatherJSON);
        sendBroadcast(broadcastIntent);
    }


}
