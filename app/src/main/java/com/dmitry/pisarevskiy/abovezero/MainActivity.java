package com.dmitry.pisarevskiy.abovezero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import static com.dmitry.pisarevskiy.abovezero.R.menu.settings;


public class MainActivity extends AppCompatActivity {


    private static String activityState;

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
                startActivity(new Intent(this,SettingsActivity.class));
                break;
            case R.id.about:
                startActivity(new Intent(this,AboutActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spCity = findViewById(R.id.spCity);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();
                if (item.equals("Добавить…")) {
                    startActivity(new Intent(MainActivity.this ,CityActivity.class));
                    parent.setVerticalScrollbarPosition(0);
                } else {
                    TextView textView = (TextView) parent.getChildAt(0);
                    textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (savedInstanceState==null) {
            showMsgAboutState("Первый запуск onCreate");
        } else {
            showMsgAboutState("Повторный запуск onCreate");
        }
    }


    private void showMsgAboutState(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
        Log.d("MainActivity",msg);
    }


    @Override
    protected void onStart() {
        super.onStart();
        showMsgAboutState("Старт");
    }


    @Override
    protected void onStop() {
        super.onStop();
        showMsgAboutState("Стоп");
    }


    @Override
    protected void onResume() {
        super.onResume();
        showMsgAboutState("Resume");
    }


    @Override
    protected void onPause() {
        super.onPause();
        showMsgAboutState("Пауза");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        showMsgAboutState("Рестарт");
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showMsgAboutState("Нажата кнопка назад");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        showMsgAboutState("Приложение закрыто");
    }
}