package com.dmitry.pisarevskiy.abovezero;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.dmitry.pisarevskiy.abovezero.R.id.lvCities;

public class CityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        Button btnOk = findViewById(R.id.btnOk);
        ListView lvCities = findViewById(R.id.lvCities);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CityActivity.this, MainActivity.class));
            }
        });

//        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
//        lvCities.setAdapter(adapter);
//        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String str = ((TextView) view).getText().toString();
//                EditText etCity = findViewById(R.id.etCity);
//                etCity.setText(str);
//            }
//        });
    }
}