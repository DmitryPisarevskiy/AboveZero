package com.dmitry.pisarevskiy.abovezero;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.dmitry.pisarevskiy.abovezero.weather.ForecastWeather;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherSample;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    protected static final float PRESSURE_MULTIPLIER_TO_MM_RT_ST = 760f / 1030;
    protected static final float PRESSURE_MULTIPLIER_TO_KPA = 0.1f;
    protected static final float WIND_MULTIPLIER_TO_KMPH = 3600f / 1000;
    protected static final float WIND_MULTIPLIER_TO_MPS = 1;
    protected static final float CONSTANT_FOR_KELVIN_SCALE = -273.15f;
    protected static float pressureMultiplier;
    protected static float windMultiplier;

    protected static final String WIND_UNIT_TAG = "Wind unit";
    protected static final String PRESSURE_UNIT_TAG = "Pressure unit";
    protected static final String WIND_SHOW_TAG = "Show wind";
    protected static final String PRESSURE_SHOW_TAG = "Pressure show";
    protected static final String NEWCITY_TAG = "New city";
    protected static final String CURRENT_WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?id=";
    protected static final String FORECAST_WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?id=";
    protected static final int NUM_OF_DATA_ITEMS = 7;
    private static final int REQUEST_SETTINGS_CODE = 1;
    private static final int REQUEST_NEWCITY_CODE = 2;
    private static final String FAIL_NETWORK_TAG = "fail network";
    private static final String API_URL = "&appid=3f371cc26311182846ffe9eeabc50393";
    protected static String degreeUnit = "°C";
    protected static String windUnit;
    protected static String pressureUnit;
    protected static boolean isHistory = false;
    protected static boolean showWind;
    protected static boolean showPressure;
    private static String activityState;

    private Spinner spCity;
    private ArrayAdapter<String> spCityAdapter;
    private SwitchCompat swForecastHistory;
    private RecyclerView rvData;
    private RVAdapterData rvAdapter;

    private final String[] TIMES_FORECAST = {"16.00", "17.00", "18.00", "19.00", "20.00", "21.00", "22.00"};
    private final String[] TIMES_HISTORY = {"09.00", "10.00", "11.00", "12.00", "13.00", "14.00", "15.00"};

    private final HashMap<String, String> citiesID = new HashMap() {{
        put("Нурдавлетово", "479561");
        put("Москва", "524894");
        put("Санкт-Петербург", "498817");
        put("Самара", "499099");
        put("Омск", "1496153");
        put("Томск", "1489425");
        put("Кострома", "543878");
    }};

    private final int[][] IMG_HISTORY = {
            {R.drawable.sunny, R.drawable.sunny, R.drawable.strong_rain, R.drawable.sunny, R.drawable.sunny, R.drawable.sunny, R.drawable.sunny},
            {R.drawable.sunny, R.drawable.week_cloudly, R.drawable.strong_rain, R.drawable.strong_rain, R.drawable.sunny, R.drawable.sunny, R.drawable.week_cloudly},
            {R.drawable.cloudly, R.drawable.strong_rain, R.drawable.strong_rain, R.drawable.strong_rain, R.drawable.strong_rain, R.drawable.cloudly, R.drawable.strong_rain},
    };

    private final float[][] TEMPERATURES_HISTORY = {
            {300, 301, 302, 303, 304, 305, 306},
            {276, 277, 278, 279, 280, 281, 282},
            {256, 257, 258, 259, 260, 270, 280}
    };

    private final float[][] PRESSURES_HISTORY = {
            {980, 990, 1000, 1010, 1020, 1030, 1040},
            {1130, 1140, 1150, 1160, 1170, 1180, 1190},
            {350, 450, 550, 650, 750, 850, 950}
    };

    private final float[][] WINDS_HISTORY = {
            {0, 0, 0, 0, 0, 0, 0},
            {3, 4, 5, 6, 7, 8, 9},
            {70, 60, 50, 40, 30, 20, 10}
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_SETTINGS_CODE);
                break;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null || resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case REQUEST_SETTINGS_CODE:
                windUnit = data.getStringExtra(WIND_UNIT_TAG);
                switch (windUnit) {
                    case ("м/с"):
                        windMultiplier = WIND_MULTIPLIER_TO_MPS;
                        break;
                    case ("км/ч"):
                        windMultiplier = WIND_MULTIPLIER_TO_KMPH;
                        break;
                }
                pressureUnit = data.getStringExtra(PRESSURE_UNIT_TAG);
                switch (pressureUnit) {
                    case ("кПа"):
                        pressureMultiplier = PRESSURE_MULTIPLIER_TO_KPA;
                        break;
                    case ("мм.рт.ст."):
                        pressureMultiplier = PRESSURE_MULTIPLIER_TO_MM_RT_ST;
                        break;
                }
                showWind = data.getBooleanExtra(WIND_SHOW_TAG, true);
                showPressure = data.getBooleanExtra(PRESSURE_SHOW_TAG, true);
                break;
            case REQUEST_NEWCITY_CODE:
                spCityAdapter.add(data.getStringExtra(NEWCITY_TAG));
                spCityAdapter.notifyDataSetChanged();
                break;
        }
        refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spCity = findViewById(R.id.spCity);
        spCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCityAdapter.addAll(getResources().getStringArray(R.array.cities_selected));
        spCity.setAdapter(spCityAdapter);
        spCityAdapter.notifyDataSetChanged();
        swForecastHistory = findViewById(R.id.swForecastHistory);

        showPressure = true;
        showWind = true;
        pressureUnit = getResources().getStringArray(R.array.pressure_unit)[0];
        windUnit = getResources().getStringArray(R.array.wind_unit)[0];
        isHistory = false;
        pressureMultiplier = PRESSURE_MULTIPLIER_TO_KPA;
        windMultiplier = WIND_MULTIPLIER_TO_MPS;
        refresh();

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, int position, long id) {
                final String item = parent.getSelectedItem().toString();
                if (item.equals("Добавить…")) {
                    startActivityForResult(new Intent(MainActivity.this, CityActivity.class), REQUEST_NEWCITY_CODE);
                    parent.setSelection(0);
                } else {
                    refresh();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        swForecastHistory.setOnCheckedChangeListener(new SwitchCompat.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHistory = isChecked;
                refresh();
            }
        });
    }

    private void refresh() {
        String item = spCity.getSelectedItem().toString();
        try {
            final Handler handler = new Handler();
            final URL urlCurrent = new URL(CURRENT_WEATHER_URL + citiesID.get(item) + API_URL);
            final URL urlForecast = new URL(FORECAST_WEATHER_URL + citiesID.get(item) + API_URL);
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) urlCurrent.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        String result = in.lines().collect(Collectors.joining("\n"));
                        Gson gson = new Gson();
                        System.out.println(result);
                        final WeatherSample currentWeather = gson.fromJson(result, WeatherSample.class);
                        urlConnection = (HttpURLConnection) urlForecast.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        result = in.lines().collect(Collectors.joining("\n"));
                        final ForecastWeather forecastWeather = gson.fromJson(result, ForecastWeather.class);
                        System.out.println(result);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int pos = (int) spCity.getSelectedItemId() > 2 ? 0 : (int) spCity.getSelectedItemId();
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                CityFragment city = CityFragment.newInstance(forecastWeather.getCity().getId(), forecastWeather.getCity().getName(), currentWeather.getMain().getTemp(), currentWeather.getWind().getSpeed(), currentWeather.getImage());
                                DataFragment history = DataFragment.newInstance(
                                        isHistory ? TIMES_HISTORY : forecastWeather.getTimes(NUM_OF_DATA_ITEMS),
                                        isHistory ? IMG_HISTORY[pos] : forecastWeather.getImages(NUM_OF_DATA_ITEMS),
                                        isHistory ? TEMPERATURES_HISTORY[pos] : forecastWeather.getTemps(NUM_OF_DATA_ITEMS),
                                        isHistory ? PRESSURES_HISTORY[pos] : forecastWeather.getPressures(NUM_OF_DATA_ITEMS),
                                        isHistory ? WINDS_HISTORY[pos] : forecastWeather.getWinds(NUM_OF_DATA_ITEMS));
                                ft.replace(R.id.flData, history);
                                ft.replace(R.id.flCityFrame, city);
                                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                ft.commit();
                            }
                        });
                    } catch (Exception e) {
                        Log.e(FAIL_NETWORK_TAG, getResources().getString(R.string.fail_network), e);
                        Snackbar.make(spCity, getResources().getString(R.string.fail_network), Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }

                }
            }).start();
        } catch (MalformedURLException e) {
            Log.e(FAIL_NETWORK_TAG, getResources().getString(R.string.incorrect_uri), e);
            Snackbar.make(spCity, getResources().getString(R.string.incorrect_uri), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

}