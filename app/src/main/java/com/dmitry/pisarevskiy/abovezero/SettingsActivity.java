package com.dmitry.pisarevskiy.abovezero;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    public static boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button btnOk = findViewById(R.id.btnOk);
        Button btnCancel = findViewById(R.id.btnCancel);
        // привязка View
        final CheckBox cbShowWindSpeed = findViewById(R.id.cbShowWindSpeed);
        final CheckBox cbShowPressure = findViewById(R.id.cbShowPressure);
        final Spinner spWindUnit = findViewById(R.id.spWindUnit);
        final Spinner spPressureUnit = findViewById(R.id.spPressureUnit);
        // инициализация значений
        cbShowWindSpeed.setChecked(MainActivity.showWind);
        cbShowPressure.setChecked(MainActivity.showPressure);
        if (MainActivity.windUnit.equals("м/с")) {
            spWindUnit.setSelection(0);
        } else {
            spWindUnit.setSelection(1);
        }
        if (MainActivity.pressureUnit.equals("кПа")) {
            spPressureUnit.setSelection(0);
        } else {
            spPressureUnit.setSelection(1);
        }
        // установка слушателей
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle(R.string.sure_before_settings)
                        .setCancelable(true)
                        .setIcon(R.drawable.ic_baseline_settings_24)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra(MainActivity.PRESSURE_SHOW_TAG, cbShowPressure.isChecked());
                                intent.putExtra(MainActivity.WIND_SHOW_TAG, cbShowWindSpeed.isChecked());
                                intent.putExtra(MainActivity.WIND_UNIT_TAG, String.valueOf(spWindUnit.getSelectedItem()));
                                intent.putExtra(MainActivity.PRESSURE_UNIT_TAG, String.valueOf(spPressureUnit.getSelectedItem()));
                                setResult(MainActivity.RESULT_OK, intent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(MainActivity.RESULT_CANCELED, new Intent());
                            }
                        })
                        .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(MainActivity.RESULT_CANCELED, new Intent());
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
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
        cbShowWindSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        cbShowPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        spWindUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spPressureUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
