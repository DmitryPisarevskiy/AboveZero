package com.dmitry.pisarevskiy.abovezero;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.dmitry.pisarevskiy.abovezero.database.App;
import com.dmitry.pisarevskiy.abovezero.database.RequestDao;
import com.dmitry.pisarevskiy.abovezero.database.RequestSource;
import com.dmitry.pisarevskiy.abovezero.weather.WeatherOneCall;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Переводные коэффициенты для температуры, давления
    protected static final float PRESSURE_MULTIPLIER_TO_MM_RT_ST = 760f / 1030;
    protected static final float PRESSURE_MULTIPLIER_TO_KPA = 0.1f;
    protected static final float WIND_MULTIPLIER_TO_KMPH = 3600f / 1000;
    protected static final float WIND_MULTIPLIER_TO_MPS = 1;
    protected static final float CONSTANT_FOR_KELVIN_SCALE = -273.15f;
    private static final int TAG_CODE_PERMISSION_LOCATION = 10;
    protected static float pressureMultiplier;
    protected static float windMultiplier;
    //Тэги
    private static final String SP_CITY_TAG = "Selected city";
    protected static final String WIND_UNIT_TAG = "Wind unit";
    protected static final String PRESSURE_UNIT_TAG = "Pressure unit";
    protected static final String WIND_SHOW_TAG = "Show wind";
    protected static final String PRESSURE_SHOW_TAG = "Pressure show";
    private static final int REQUEST_SETTINGS_CODE = 1;
    // Приемники широковещательных сообщений
    public static final String CONNECTIVITY_ACTION_LOLLIPOP = "com.dmitry.pisarevskiy.abovezero.CONNECTIVITY_ACTION_LOLLIPOP";
    private NetReceiver netReceiver = new NetReceiver();
    private BroadcastReceiver batteryReciever = new BatteryReciever();
    //Доступ к OpenWeather
    private WeatherOneCall weatherOneCall;
    private LocationManager locationManager;
    private Location location;
    private static final String API = "3f371cc26311182846ffe9eeabc50393";
    protected static final String BASE_URL = "https://api.openweathermap.org/";
    private OpenWeather openWeather;
    //Настройки
    private SharedPreferences sharedPref;
    protected static String degreeUnit = "°C";
    protected static String windUnit;
    protected static String pressureUnit;
    protected static boolean isDaily = false;
    protected static boolean showWind;
    protected static boolean showPressure;
    // Элементы интерфйеса
    private Spinner spCity;
    protected ArrayAdapter<String> spCityAdapter;
    private MySwitchView switchForecastHistory;
    //Для статистики погоды
    private final HashMap<String, City> cities = new HashMap() {{
        put("Нурдавлетово", new City(479561, "Нурдавлетово", 55.90773f,53.383652f));
        put("Москва", new City(524894, "Москва", 37.606667f,55.761665f));
        put("Санкт-Петербург", new City(498817, "Санкт-Петербург", 30.264168f,59.894444f));
        put("Самара", new City(499099, "Самара", 50.150002f,53.200001f));
        put("Омск", new City(1496153, "Омск", 73.400002f,55.0f));
        put("Томск", new City(1489425, "Томск", 84.966667f,56.5f));
        put("Кострома", new City(543878, "Кострома", 40.934444f,57.770832f));
    }};
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
                final BottomCityChoiceFragment bottomCityChoiceFragment = new BottomCityChoiceFragment();
                bottomCityChoiceFragment.show(getSupportFragmentManager(), "dialog fragment");
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Установка настроек
        sharedPref = getPreferences(MODE_PRIVATE);
        showPressure = sharedPref.getBoolean(PRESSURE_SHOW_TAG, true);
        showWind = sharedPref.getBoolean(WIND_SHOW_TAG, true);
        pressureUnit = sharedPref.getString(PRESSURE_UNIT_TAG, getResources().getStringArray(R.array.pressure_unit)[0]);
        windUnit = sharedPref.getString(WIND_UNIT_TAG, getResources().getStringArray(R.array.wind_unit)[0]);
        isDaily = false;
        setMultipliers();
        // Инициализация
        initDrawer();
        initRetrofit();
        initGUI();
        initGetToken();
        initNotificationChannel();
        initRecievers();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    TAG_CODE_PERMISSION_LOCATION);
        }
        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            finish();
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                1000 * 10, 10, new LocationListener() {
//                    @Override
//                    public void onLocationChanged(@NonNull Location l) {
//                        location = l;
//                        Log.d("Location",String.format("Lat - %.2f, Lon - %.2f", l.getLatitude(), l.getLongitude()));
//                    }
//                    @Override
//                    public void onProviderEnabled(@NonNull String provider) {
//                        checkEnabled();
//                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
//                                            Manifest.permission.ACCESS_FINE_LOCATION,
//                                            Manifest.permission.ACCESS_COARSE_LOCATION },
//                                    TAG_CODE_PERMISSION_LOCATION);
//                        } else {
//                            location = locationManager.getLastKnownLocation(provider);
//                            Log.d("Location",String.format("Lat - %.2f, Lon - %.2f", location.getLatitude(), location.getLongitude()));
//                        }
//                    }
//                    @Override
//                    public void onProviderDisabled(@NonNull String provider) {
//                        checkEnabled();
//                    }
//                });
//        checkEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void checkEnabled() {
        Log.d("location", String.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
    }

    private void initRecievers() {
        registerReceiver(batteryReciever, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(CONNECTIVITY_ACTION_LOLLIPOP);
        registerReceiver(netReceiver, intentFilter);
        registerConnectivityActionLollipop();
    }

    private void initGUI() {
        // Привязка элементов
        spCity = findViewById(R.id.spCity);
        spCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCityAdapter.addAll(getResources().getStringArray(R.array.cities_selected));
        spCity.setAdapter(spCityAdapter);
        spCityAdapter.notifyDataSetChanged();
        switchForecastHistory = findViewById(R.id.switchForecastHistory);
        // Установка слушателей
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
                isDaily = switchForecastHistory.isRight();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DataFragment data = DataFragment.newInstance(
                        isDaily ? weatherOneCall.getDays() : weatherOneCall.getHours(),
                        isDaily ? weatherOneCall.getDaysImages() : weatherOneCall.getHoursImages(),
                        isDaily ? weatherOneCall.getDaysTemps() : weatherOneCall.getHoursTemps(),
                        isDaily ? weatherOneCall.getDaysPressures() : weatherOneCall.getHoursPressures(),
                        isDaily ? weatherOneCall.getDaysWinds() : weatherOneCall.getHoursWinds());
                ft.replace(R.id.flData, data);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
        spCity.setSelection(sharedPref.getInt(SP_CITY_TAG, 0));
        RequestDao requestDao = App
                .getInstance()
                .getRequestDao();
        requestSource = new RequestSource(requestDao);
    }

    private void refresh() {
        final String item = spCity.getSelectedItem().toString();
        openWeather.loadOneCallWeather(String.valueOf(cities.get(item).getLat()),String.valueOf(cities.get(item).getLon()), API)
                .enqueue(new Callback<WeatherOneCall>() {
                    @Override
                    public void onResponse(Call<WeatherOneCall> call, Response<WeatherOneCall> response) {
                        if (response.body()!=null) {
                            weatherOneCall = response.body();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            CityFragment city = CityFragment.newInstance(
                                    cities.get(item).getId(),
                                    spCity.getSelectedItem().toString(),
                                    response.body().getCurrent().getTemp(),
                                    response.body().getCurrent().getWindSpeed(),
                                    response.body().getCurrent().getImage());
                            DataFragment data = DataFragment.newInstance(
                                    isDaily ? response.body().getDays() : response.body().getHours(),
                                    isDaily ? response.body().getDaysImages() : response.body().getHoursImages(),
                                    isDaily ? response.body().getDaysTemps() : response.body().getHoursTemps(),
                                    isDaily ? response.body().getDaysPressures() : response.body().getHoursPressures(),
                                    isDaily ? response.body().getDaysWinds() : response.body().getHoursWinds());
                            ft.replace(R.id.flData, data);
                            ft.replace(R.id.flCityFrame, city);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.commit();
                            com.dmitry.pisarevskiy.abovezero.database.Request request = new com.dmitry.pisarevskiy.abovezero.database.Request();
                            Date date = new java.util.Date(response.body().getCurrent().getDt());
                            SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", Locale.US);
                            request.date = sdf.format(date);
                            request.city = spCity.getSelectedItem().toString();
                            request.temperature = response.body().getCurrent().getTemp()+CONSTANT_FOR_KELVIN_SCALE;
                            requestSource.addRequest(request);
                        }
                    }
                    @Override
                    public void onFailure(Call<WeatherOneCall> call, Throwable t) {
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
//        openWeather.loadForecastWeather(String.valueOf(cities.get(item).getId()),API)
//                .enqueue(new Callback<WeatherRequest>() {
//                    @Override
//                    public void onResponse(Call<WeatherRequest> call, Response<WeatherRequest> response) {
//                        if (response.body()!=null) {
//                            int pos = (int) spCity.getSelectedItemId() > 2 ? 0 : (int) spCity.getSelectedItemId();
//                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
////                            DataFragment data = DataFragment.newInstance(
////                                    isDaily ? response.body().getTimes(NUM_OF_DATA_ITEMS) : response.body().getTimes(NUM_OF_DATA_ITEMS),
////                                    isDaily ? response.body().getImages(NUM_OF_DATA_ITEMS) : response.body().getImages(NUM_OF_DATA_ITEMS),
////                                    isDaily ? response.body().getTemps(NUM_OF_DATA_ITEMS) : response.body().getTemps(NUM_OF_DATA_ITEMS),
////                                    isDaily ? response.body().getPressures(NUM_OF_DATA_ITEMS) : response.body().getPressures(NUM_OF_DATA_ITEMS),
////                                    isDaily ? response.body().getWinds(NUM_OF_DATA_ITEMS) : response.body().getWinds(NUM_OF_DATA_ITEMS));
////                            ft.replace(R.id.flData, data);
//                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                            ft.commit();
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<WeatherRequest> call, Throwable t) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle(R.string.fail_network)
//                                .setCancelable(false)
//                                .setIcon(R.drawable.kompas)
//                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                    }
//                                });
//                        AlertDialog alert = builder.create();
//                        alert.show();
//                    }
//                });
    }

    private void initDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        savePreferences(sharedPref);
        unregisterReceiver(batteryReciever);
        unregisterReceiver(netReceiver);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void registerConnectivityActionLollipop() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Intent intent = new Intent(CONNECTIVITY_ACTION_LOLLIPOP);
                intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                sendBroadcast(intent);
            }
            @Override
            public void onLost(Network network) {
                Intent intent = new Intent(CONNECTIVITY_ACTION_LOLLIPOP);
                intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
                sendBroadcast(intent);
            }
        });
    }

    private void initGetToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("PushMessage", "getInstanceId failed", task.getException());
                            return;
                        }
                    }
                });
    }
}