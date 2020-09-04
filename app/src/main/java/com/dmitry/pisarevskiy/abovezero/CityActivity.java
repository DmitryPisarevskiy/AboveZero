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

import java.util.Arrays;


public class CityActivity extends AppCompatActivity {
    protected static EditText etCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        etCity = findViewById(R.id.etCity);
        Button btnOk = findViewById(R.id.btnOk);
        RecyclerView rvCities = findViewById(R.id.rvCities);
        RVAdapterCities rvAdapterCities = new RVAdapterCities(Arrays.asList(getResources().getStringArray(R.array.cities)));
        rvCities.setLayoutManager(new LinearLayoutManager(this));
        rvCities.setAdapter(rvAdapterCities);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCity.getText()!=null) {
                    Intent intent = new Intent();
                    intent.putExtra(MainActivity.NEWCITY_TAG, etCity.getText().toString());
                    setResult(MainActivity.RESULT_OK, intent);
                }
                finish();
            }
        });

    }
}