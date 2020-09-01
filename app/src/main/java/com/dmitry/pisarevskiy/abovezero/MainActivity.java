package com.dmitry.pisarevskiy.abovezero;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import static com.dmitry.pisarevskiy.abovezero.R.menu.settings;


public class MainActivity extends AppCompatActivity {

    protected static final String WIND_UNIT_TAG = "Wind unit";
    protected static final String PRESSURE_UNIT_TAG = "Pressure unit";
    protected static final String WIND_SHOW_TAG = "Show wind";
    protected static final String PRESSURE_SHOW_TAG = "Pressure show";
    private static final int REQUEST_SETTINGS_CODE = 1;
    protected static String degreeUnit="°C";
    protected static String windUnit;
    protected static String pressureUnit;
    private static boolean showWind;
    private static boolean showPressure;
    private static String activityState;
    private TextView tvTemp;
    private TextView tvTemp1;
    private TextView tvTemp2;
    private TextView tvTemp3;
    private TextView tvTemp4;
    private TextView tvTemp5;
    private TextView tvTemp6;
    private TextView tvTemp7;
    private TextView tvWind1;
    private TextView tvWind2;
    private TextView tvWind3;
    private TextView tvWind4;
    private TextView tvWind5;
    private TextView tvWind6;
    private TextView tvWind7;
    private TextView tvPressure1;
    private TextView tvPressure2;
    private TextView tvPressure3;
    private TextView tvPressure4;
    private TextView tvPressure5;
    private TextView tvPressure6;
    private TextView tvPressure7;

    private final String[] tempsNurdavletovo = {"30°C","30°C", "34°C", "35°C","31°C","32°C","33°C"};
    private final String[] tempsMoscow = {"10°C","11°C", "10°C", "8°C","7°C","6°C","10°C"};
    private final String[] tempsPeter = {"-10°C","0°C", "5°C", "7°C","10°C","15°C","20°C"};

    private final String[] pressNurdavletovo = {"105кПа","105кПа", "105кПа", "105кПа","105кПа","105кПа","105кПа"};
    private final String[] pressMoscow = {"120кПа","125кПа", "135кПа", "145кПа","155кПа","165кПа","175кПа"};
    private final String[] pressPeter = {"105кПа","100кПа", "95кПа", "85кПа","75кПа","65кПа","55кПа"};

    private final String[] windNurdavletovo = {"0м/с","0м/с", "0м/с", "0м/с","0м/с","0м/с","0м/с"};
    private final String[] windMoscow = {"10м/с","20м/с", "10м/с", "20м/с","10м/с","20м/с","10м/с"};
    private final String[] windPeter = {"0м/с","5м/с", "0м/с", "5м/с","0м/с","5м/с","0м/с"};

//1. Переведите ваше приложение на Фрагменты по аналогии с CityInfo.
//2. * Приложение FragmentManager содержит ошибку, попробуйте дважды добавить один фрагмент и приложение упадет. Исправьте этот баг.
//3. * Изучите шаблон «Наблюдатель»

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(settings, menu);
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
        if (data == null) {
            return;
        }
        windUnit = data.getStringExtra(WIND_UNIT_TAG);
        pressureUnit = data.getStringExtra(PRESSURE_UNIT_TAG);
        showWind = data.getBooleanExtra(WIND_SHOW_TAG, true);
        showPressure = data.getBooleanExtra(PRESSURE_SHOW_TAG, true);
        refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Spinner spCity = findViewById(R.id.spCity);
        tvTemp = findViewById(R.id.tvWindSpeed);
        tvTemp1 = findViewById(R.id.tvTemp1);
        tvTemp2 = findViewById(R.id.tvTemp2);
        tvTemp3 = findViewById(R.id.tvTemp3);
        tvTemp4 = findViewById(R.id.tvTemp4);
        tvTemp5 = findViewById(R.id.tvTemp5);
        tvTemp6 = findViewById(R.id.tvTemp6);
        tvTemp7 = findViewById(R.id.tvTemp7);
        tvWind1 = findViewById(R.id.tvWind1);
        tvWind2 = findViewById(R.id.tvWind2);
        tvWind3 = findViewById(R.id.tvWind3);
        tvWind4 = findViewById(R.id.tvWind4);
        tvWind5 = findViewById(R.id.tvWind5);
        tvWind6 = findViewById(R.id.tvWind6);
        tvWind7 = findViewById(R.id.tvWind7);
        tvPressure1 = findViewById(R.id.tvPressure1);
        tvPressure2 = findViewById(R.id.tvPressure2);
        tvPressure3 = findViewById(R.id.tvPressure3);
        tvPressure4 = findViewById(R.id.tvPressure4);
        tvPressure5 = findViewById(R.id.tvPressure5);
        tvPressure6 = findViewById(R.id.tvPressure6);
        tvPressure7 = findViewById(R.id.tvPressure7);

        final RecyclerView rvData = findViewById(R.id.rvData);
        rvData.setLayoutManager(new LinearLayoutManager(this));
        final RVAdapter rvAdapter = new RVAdapter(Arrays.asList(tempsNurdavletovo),Arrays.asList(pressNurdavletovo),Arrays.asList(windNurdavletovo));
        rvData.setAdapter(rvAdapter);

        showPressure = true;
        showWind = true;
        pressureUnit = getResources().getStringArray(R.array.pressure_unit)[0];
        windUnit = getResources().getStringArray(R.array.wind_unit)[0];
        refresh();

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();
                if (item.equals("Добавить…")) {
                    startActivity(new Intent(MainActivity.this, CityActivity.class));
                    parent.setVerticalScrollbarPosition(0);
                } else {
//                    TextView textView = (TextView) parent.getSelectedView();
//                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
//                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    CityFragment city = CityFragment.newInstance((int)parent.getSelectedItemId(),item);
                    ft.replace(R.id.cityFrame, city);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                    switch ((int)parent.getSelectedItemId()) {
                        case 0:
                            rvAdapter.setPressures(Arrays.asList(pressNurdavletovo));
                            rvAdapter.setTemperatures(Arrays.asList(tempsNurdavletovo));
                            rvAdapter.setWinds(Arrays.asList(windNurdavletovo));
                            break;
                        case 1:
                            rvAdapter.setPressures(Arrays.asList(pressMoscow));
                            rvAdapter.setTemperatures(Arrays.asList(tempsMoscow));
                            rvAdapter.setWinds(Arrays.asList(windMoscow));
                            break;
                        case 2:
                            rvAdapter.setPressures(Arrays.asList(pressPeter));
                            rvAdapter.setTemperatures(Arrays.asList(tempsPeter));
                            rvAdapter.setWinds(Arrays.asList(windPeter));
                            break;
                        default:
                            rvAdapter.setPressures(Arrays.asList(pressNurdavletovo));
                            rvAdapter.setTemperatures(Arrays.asList(tempsNurdavletovo));
                            rvAdapter.setWinds(Arrays.asList(windNurdavletovo));
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void refresh() {
        tvTemp1.setText((getResources().getString(R.string.temp_example1) + " " + degreeUnit));
        tvTemp2.setText((getResources().getString(R.string.temp_example2) + " " + degreeUnit));
        tvTemp3.setText((getResources().getString(R.string.temp_example3) + " " + degreeUnit));
        tvTemp4.setText((getResources().getString(R.string.temp_example4) + " " + degreeUnit));
        tvTemp5.setText((getResources().getString(R.string.temp_example5) + " " + degreeUnit));
        tvTemp6.setText((getResources().getString(R.string.temp_example6) + " " + degreeUnit));
        tvTemp7.setText((getResources().getString(R.string.temp_example7) + " " + degreeUnit));
        tvWind1.setText((getResources().getString(R.string.wind_example1) + " " + windUnit));
        tvWind2.setText((getResources().getString(R.string.wind_example2) + " " + windUnit));
        tvWind3.setText((getResources().getString(R.string.wind_example3) + " " + windUnit));
        tvWind4.setText((getResources().getString(R.string.wind_example4) + " " + windUnit));
        tvWind5.setText((getResources().getString(R.string.wind_example5) + " " + windUnit));
        tvWind6.setText((getResources().getString(R.string.wind_example6) + " " + windUnit));
        tvWind7.setText((getResources().getString(R.string.wind_example7) + " " + windUnit));
        tvPressure1.setText((getResources().getString(R.string.pressure_example1) + pressureUnit));
        tvPressure2.setText((getResources().getString(R.string.pressure_example2) + pressureUnit));
        tvPressure3.setText((getResources().getString(R.string.pressure_example3) + pressureUnit));
        tvPressure4.setText((getResources().getString(R.string.pressure_example4) + pressureUnit));
        tvPressure5.setText((getResources().getString(R.string.pressure_example5) + pressureUnit));
        tvPressure6.setText((getResources().getString(R.string.pressure_example6) + pressureUnit));
        tvPressure7.setText((getResources().getString(R.string.pressure_example7) + pressureUnit));
        if (showWind) {
            tvWind1.setVisibility(View.VISIBLE);
            tvWind2.setVisibility(View.VISIBLE);
            tvWind3.setVisibility(View.VISIBLE);
            tvWind4.setVisibility(View.VISIBLE);
            tvWind5.setVisibility(View.VISIBLE);
            tvWind6.setVisibility(View.VISIBLE);
            tvWind7.setVisibility(View.VISIBLE);
        } else {
            tvWind1.setVisibility(View.INVISIBLE);
            tvWind2.setVisibility(View.INVISIBLE);
            tvWind3.setVisibility(View.INVISIBLE);
            tvWind4.setVisibility(View.INVISIBLE);
            tvWind5.setVisibility(View.INVISIBLE);
            tvWind6.setVisibility(View.INVISIBLE);
            tvWind7.setVisibility(View.INVISIBLE);
        }
        if (showPressure) {
            tvPressure1.setVisibility(View.VISIBLE);
            tvPressure2.setVisibility(View.VISIBLE);
            tvPressure3.setVisibility(View.VISIBLE);
            tvPressure4.setVisibility(View.VISIBLE);
            tvPressure5.setVisibility(View.VISIBLE);
            tvPressure6.setVisibility(View.VISIBLE);
            tvPressure7.setVisibility(View.VISIBLE);
        } else {
            tvPressure1.setVisibility(View.INVISIBLE);
            tvPressure2.setVisibility(View.INVISIBLE);
            tvPressure3.setVisibility(View.INVISIBLE);
            tvPressure4.setVisibility(View.INVISIBLE);
            tvPressure5.setVisibility(View.INVISIBLE);
            tvPressure6.setVisibility(View.INVISIBLE);
            tvPressure7.setVisibility(View.INVISIBLE);
        }
    }
}