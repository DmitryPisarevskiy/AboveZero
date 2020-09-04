package com.dmitry.pisarevskiy.abovezero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.regex.Pattern;


public class CityActivity extends AppCompatActivity {
    protected static TextInputEditText tietCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        tietCity = findViewById(R.id.tietCity);
        final Pattern checkCityName= Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}$");
        Button btnOk = findViewById(R.id.btnOk);
        Button btnCancel = findViewById(R.id.btnCancel);
        RecyclerView rvCities = findViewById(R.id.rvCities);
        RVAdapterCities rvAdapterCities = new RVAdapterCities(Arrays.asList(getResources().getStringArray(R.array.cities)));
        rvCities.setLayoutManager(new LinearLayoutManager(this));
        rvCities.setAdapter(rvAdapterCities);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tietCity.getText()!=null) {
                    Intent intent = new Intent();
                    if (checkCityName.matcher(tietCity.getText().toString()).matches()) {
                        intent.putExtra(MainActivity.NEWCITY_TAG, tietCity.getText().toString());
                        setResult(MainActivity.RESULT_OK, intent);
                        finish();
                    } else {
                        Snackbar.make(v,"Введенное название неверно",
                                Snackbar.LENGTH_LONG).setAction("Понятно",new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                setResult(MainActivity.RESULT_CANCELED, null);
                                finish();
                            }
                        }).show();
                    }
                }
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

        tietCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    return;
                }
                TextView tv = (TextView) v;
                validate(tv, checkCityName, "Название должно начинаться с большой буквы");
            }
        });
    }

    private void validate(TextView tv, Pattern checkCityName, String s) {
        String value = tv.getText().toString();
        if (checkCityName.matcher(value).matches()) {
            tv.setError(null);
        } else {
            tv.setError(s);
        }
    }
}