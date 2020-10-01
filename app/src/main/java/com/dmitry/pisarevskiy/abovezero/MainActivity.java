package com.dmitry.pisarevskiy.abovezero;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dmitry.pisarevskiy.abovezero.database.App;
import com.dmitry.pisarevskiy.abovezero.database.RequestDao;
import com.dmitry.pisarevskiy.abovezero.database.RequestSource;
import com.dmitry.pisarevskiy.abovezero.weather.Request;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherSample;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //Переводные коэффициенты для температуры, давления
    protected static final float PRESSURE_MULTIPLIER_TO_MM_RT_ST = 760f / 1030;
    protected static final float PRESSURE_MULTIPLIER_TO_KPA = 0.1f;
    protected static final float WIND_MULTIPLIER_TO_KMPH = 3600f / 1000;
    protected static final float WIND_MULTIPLIER_TO_MPS = 1;
    protected static final float CONSTANT_FOR_KELVIN_SCALE = -273.15f;
    protected static float pressureMultiplier;
    protected static float windMultiplier;
    //Тэги
    private static final String SP_CITY_TAG = "Selected city";
    public static final String BROADCAST_DATA_LOADED = "data loaded";
    protected static final String WIND_UNIT_TAG = "Wind unit";
    protected static final String PRESSURE_UNIT_TAG = "Pressure unit";
    protected static final String WIND_SHOW_TAG = "Show wind";
    protected static final String PRESSURE_SHOW_TAG = "Pressure show";
    private static final int REQUEST_SETTINGS_CODE = 1;
    //Доступ к OpenWeather
    private static final String API = "3f371cc26311182846ffe9eeabc50393";
    protected static final String BASE_URL = "https://api.openweathermap.org/";
    private OpenWeather openWeather;
    //Настройки
    protected static String degreeUnit = "°C";
    protected static String windUnit;
    protected static String pressureUnit;
    protected static boolean isHistory = false;
    protected static boolean showWind;
    protected static boolean showPressure;
    // Элементы интерфйеса
    private Spinner spCity;
    protected ArrayAdapter<String> spCityAdapter;
    private MySwitchView switchForecastHistory;
    //Для статистики погоды
    //TODO переделать исторические данные на получение данных с OpenWeather
    protected static final int NUM_OF_DATA_ITEMS = 7;
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
    private RequestSource requestSource;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
            case R.id.newCity:
                String s = spCity.getSelectedItem().toString();
                final BottomCityChoiceFragment bottomCityChoiceFragment = new BottomCityChoiceFragment();
                bottomCityChoiceFragment.show(getSupportFragmentManager(),"dialog fragment");
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
        if (requestCode == REQUEST_SETTINGS_CODE) {
            windUnit = data.getStringExtra(WIND_UNIT_TAG);
            pressureUnit = data.getStringExtra(PRESSURE_UNIT_TAG);
            setMultipliers();
            showWind = data.getBooleanExtra(WIND_SHOW_TAG, true);
            showPressure = data.getBooleanExtra(PRESSURE_SHOW_TAG, true);
        }
        refresh();
    }

    private void setMultipliers() {
        switch (windUnit) {
            case ("м/с"):
                windMultiplier = WIND_MULTIPLIER_TO_MPS;
                break;
            case ("км/ч"):
                windMultiplier = WIND_MULTIPLIER_TO_KMPH;
                break;
        }
        switch (pressureUnit) {
            case ("кПа"):
                pressureMultiplier = PRESSURE_MULTIPLIER_TO_KPA;
                break;
            case ("мм.рт.ст."):
                pressureMultiplier = PRESSURE_MULTIPLIER_TO_MM_RT_ST;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        // Инициализация
        initDrawer();
        initRetrofit();
        initGUI();
        // Установка настроек
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        showPressure = sharedPref.getBoolean(PRESSURE_SHOW_TAG, true);
        showWind = sharedPref.getBoolean(WIND_SHOW_TAG, true);
        pressureUnit = sharedPref.getString(PRESSURE_UNIT_TAG, getResources().getStringArray(R.array.pressure_unit)[0]);
        windUnit = sharedPref.getString(WIND_UNIT_TAG, getResources().getStringArray(R.array.wind_unit)[0]);
        isHistory = false;
        setMultipliers();
        refresh();
        // Установка слушателей на элементы Activity
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, int position, long id) {
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        switchForecastHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHistory = switchForecastHistory.isRight();
                refresh();
            }
        });
        spCity.setSelection(sharedPref.getInt(SP_CITY_TAG, 0));
        RequestDao requestDao = App
                .getInstance()
                .getRequestDao();
        requestSource = new RequestSource(requestDao);
    }

    private void initGUI() {
        spCity = findViewById(R.id.spCity);
        spCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCityAdapter.addAll(getResources().getStringArray(R.array.cities_selected));
        spCity.setAdapter(spCityAdapter);
        spCityAdapter.notifyDataSetChanged();
        switchForecastHistory = findViewById(R.id.switchForecastHistory);
    }

    private void refresh() {
        final String item = spCity.getSelectedItem().toString();
        openWeather.loadCurrentWeather(citiesID.get(item), API)
                .enqueue(new Callback<WeatherSample>() {
                    @Override
                    public void onResponse(Call<WeatherSample> call, Response<WeatherSample> response) {
                        if (response.body()!=null) {
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            CityFragment city = CityFragment.newInstance(0, spCity.getSelectedItem().toString(), response.body().getMain().getTemp(), response.body().getWind().getSpeed(), response.body().getImage());
                            ft.replace(R.id.flCityFrame, city);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.commit();
                            com.dmitry.pisarevskiy.abovezero.database.Request request = new com.dmitry.pisarevskiy.abovezero.database.Request();
                            Date date = new java.util.Date(response.body().getDt());
                            SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", Locale.US);
                            request.date = sdf.format(date);;
                            request.city = spCity.getSelectedItem().toString();
                            request.temperature = response.body().getMain().getTemp()+CONSTANT_FOR_KELVIN_SCALE;
                            requestSource.addRequest(request);
                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherSample> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(R.string.fail_network)
                                .setCancelable(false)
                                .setIcon(R.drawable.kompas)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
        openWeather.loadForecastWeather(citiesID.get(item),API)
                .enqueue(new Callback<Request>() {
                    @Override
                    public void onResponse(Call<Request> call, Response<Request> response) {
                        if (response.body()!=null) {
                            int pos = (int) spCity.getSelectedItemId() > 2 ? 0 : (int) spCity.getSelectedItemId();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            DataFragment data = DataFragment.newInstance(
                                    isHistory ? TIMES_HISTORY : response.body().getTimes(NUM_OF_DATA_ITEMS),
                                    isHistory ? IMG_HISTORY[pos] : response.body().getImages(NUM_OF_DATA_ITEMS),
                                    isHistory ? TEMPERATURES_HISTORY[pos] : response.body().getTemps(NUM_OF_DATA_ITEMS),
                                    isHistory ? PRESSURES_HISTORY[pos] : response.body().getPressures(NUM_OF_DATA_ITEMS),
                                    isHistory ? WINDS_HISTORY[pos] : response.body().getWinds(NUM_OF_DATA_ITEMS));
                            ft.replace(R.id.flData, data);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.commit();
                        }
                    }
                    @Override
                    public void onFailure(Call<Request> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(R.string.fail_network)
                                .setCancelable(false)
                                .setIcon(R.drawable.kompas)
                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
    }

    private void initDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.program_image);
        Picasso.get()
                .load(getString(R.string.program_image_url))
                .into(imageView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                String url = getString(R.string.geekbrains_ru);
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(browser);
                break;
            case R.id.nav_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case R.id.nav_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), REQUEST_SETTINGS_CODE);
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initRetrofit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        savePreferences(sharedPref);
    }

    private void savePreferences(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(PRESSURE_SHOW_TAG, showPressure);
        editor.putBoolean(WIND_SHOW_TAG, showWind);
        editor.putString(WIND_UNIT_TAG,windUnit);
        editor.putString(PRESSURE_UNIT_TAG,pressureUnit);
        editor.putInt(SP_CITY_TAG, spCity.getSelectedItemPosition());
        editor.apply();
    }
}