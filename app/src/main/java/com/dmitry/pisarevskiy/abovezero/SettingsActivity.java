package com.dmitry.pisarevskiy.abovezero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;


public class SettingsActivity extends Activity {
    public static boolean ok=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button btnOk = findViewById(R.id.btnOk);
        Button btnCancel = findViewById(R.id.btnCancel);
        final CheckBox showWindSpeed = findViewById(R.id.cbShowWindSpeed);
        final CheckBox showPressure = findViewById(R.id.cbShowPressure);
        final Spinner windUnit = findViewById(R.id.spWindUnit);
        final Spinner pressureUnit = findViewById(R.id.spPressureUnit);
        final RadioGroup rgMode = findViewById(R.id.rgMode);
        final RadioButton rbNightMode = findViewById(R.id.radioButton2);
        final SingleTon settings = SingleTon.getInstance();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(MainActivity.PRESSURE_SHOW_TAG, showPressure.isChecked());
                intent.putExtra(MainActivity.WIND_SHOW_TAG, showWindSpeed.isChecked());
                intent.putExtra(MainActivity.WIND_UNIT_TAG, String.valueOf(windUnit.getSelectedItem()));
                intent.putExtra(MainActivity.PRESSURE_UNIT_TAG, String.valueOf(pressureUnit.getSelectedItem()));
                setResult(MainActivity.RESULT_OK, intent);
                Snackbar.make(v,"Вы уверены, что хотите применить настройки?",
                        Snackbar.LENGTH_LONG).setAction("OK",new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(MainActivity.RESULT_CANCELED, intent);
                finish();
            }
        });

        showWindSpeed.setChecked(settings.isShowWindSpeed());
        showPressure.setChecked(settings.isShowPressure());
        windUnit.setSelection(settings.getWindSpeedUnit());
        pressureUnit.setSelection(settings.getPressureUnit());
        if (settings.isNightMode()) {
            rbNightMode.setChecked(true);
        }
        windUnit.setSelection(settings.getWindSpeedUnit());
        pressureUnit.setSelection(settings.getPressureUnit());

        showWindSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setShowWindSpeed(showWindSpeed.isChecked());
            }
        });

        showPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setShowPressure(showPressure.isChecked());
            }
        });

        rgMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case 0:
                        settings.setNightMode(false);
                        break;
                    case R.id.radioButton2:
                        settings.setNightMode(true);
                        break;
                }
            }
        });

        windUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setWindSpeedUnit(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        pressureUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setPressureUnit(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
