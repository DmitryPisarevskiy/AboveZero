package com.dmitry.pisarevskiy.abovezero;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import static com.dmitry.pisarevskiy.abovezero.R.menu.settings;

//* В погодном приложении сделайте добавление населенного пункта в RecyclerView


public class MainActivity extends AppCompatActivity {

    protected static final String WIND_UNIT_TAG = "Wind unit";
    protected static final String PRESSURE_UNIT_TAG = "Pressure unit";
    protected static final String WIND_SHOW_TAG = "Show wind";
    protected static final String PRESSURE_SHOW_TAG = "Pressure show";
    protected static final String NEWCITY_TAG = "New city";
    private static final int REQUEST_SETTINGS_CODE = 1;
    private static final int REQUEST_NEWCITY_CODE = 2;
    protected static String degreeUnit="°C";
    protected static String windUnit;
    protected static String pressureUnit;
    protected static boolean isHistory=false;
    protected static boolean showWind;
    protected static boolean showPressure;
    private static String activityState;

    private Spinner spCity;
    private ArrayAdapter<String> spCityAdapter;
    private SwitchCompat swForecastHistory;
    private RecyclerView rvData;
    private RVAdapterData rvAdapter;

    private final String[] TIMES_FORECAST = {"16.00","17.00","18.00","19.00","20.00","21.00","22.00"};
    private final String[] TIMES_HISTORY = {"09.00","10.00","11.00","12.00","13.00","14.00","15.00"};

    private final int[][] IMG_FORECAST = {
            {R.drawable.sunny,R.drawable.sunny,R.drawable.sunny, R.drawable.sunny, R.drawable.sunny,R.drawable.sunny, R.drawable.sunny},
            {R.drawable.cloudl,R.drawable.week_cloudly,R.drawable.sunny, R.drawable.strong_rain, R.drawable.sunny,R.drawable.sunny, R.drawable.strong_rain},
            {R.drawable.cloudl,R.drawable.week_rain,R.drawable.cloudl, R.drawable.strong_rain, R.drawable.week_rain,R.drawable.cloudl, R.drawable.strong_rain},
    };

    private final int[][] IMG_HISTORY = {
            {R.drawable.sunny,R.drawable.sunny,R.drawable.strong_rain, R.drawable.sunny, R.drawable.sunny,R.drawable.sunny,R.drawable.sunny},
            {R.drawable.sunny,R.drawable.week_cloudly,R.drawable.strong_rain, R.drawable.strong_rain, R.drawable.sunny,R.drawable.sunny,R.drawable.week_cloudly},
            {R.drawable.cloudl,R.drawable.strong_rain,R.drawable.strong_rain, R.drawable.strong_rain, R.drawable.strong_rain,R.drawable.cloudl,R.drawable.strong_rain},
    };

    private final String[][] TEMPERATURES_FORECAST = {
            {"30","30", "34", "35","31","32","33"},
            {"10","11", "10", "8","7","6","10"},
            {"-10","0", "5", "7","10","15","20"}
    };

    private final String[][] PRESSURES_FORECAST = {
            {"105","105", "105", "105","105","105","105"},
            {"120","125", "135", "145","155","165","175"},
            {"105","100", "95", "85","75","65","55"}
    };

    private final String[][] WINDS_FORECAST = {
            {"0","0", "0", "0","0","0","0"},
            {"10","20", "10", "20","10","20","10"},
            {"0","5", "0", "5","0","5","0"}
    };

    private final String[][] TEMPERATURES_HISTORY = {
            {"23","24", "25", "26","27","28","29"},
            {"3","4", "5", "6","7","8","9"},
            {"-17","-16", "-15", "-14","-13","-12","-11"}
    };

    private final String[][] PRESSURES_HISTORY = {
            {"98","99", "100", "101","102","103","104"},
            {"113","114", "115", "116","117","118","119"},
            {"35","45", "55", "65","75кПа","85","95"}
    };

    private final String[][] WINDS_HISTORY = {
            {"0","0", "0", "0","0","0","0"},
            {"3","4", "5", "6","7","8","9"},
            {"70","60", "50", "40","30","20","10"}
    };

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
        switch (requestCode) {
            case REQUEST_SETTINGS_CODE:
                windUnit = data.getStringExtra(WIND_UNIT_TAG);
                pressureUnit = data.getStringExtra(PRESSURE_UNIT_TAG);
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
        spCityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
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
        refresh();

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();
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
        int pos = (int)spCity.getSelectedItemId()>2?0:(int)spCity.getSelectedItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CityFragment city = CityFragment.newInstance(pos,spCity.getSelectedItem().toString());
        DataFragment history = DataFragment.newInstance(
                isHistory?TIMES_HISTORY:TIMES_FORECAST,
                isHistory?IMG_HISTORY[pos]:IMG_FORECAST[pos],
                isHistory?TEMPERATURES_HISTORY[pos]:TEMPERATURES_FORECAST[pos],
                isHistory?PRESSURES_HISTORY[pos]:PRESSURES_FORECAST[pos],
                isHistory?WINDS_HISTORY[pos]:WINDS_FORECAST[pos]);
        ft.replace(R.id.flData,history);
        ft.replace(R.id.flCityFrame, city);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}