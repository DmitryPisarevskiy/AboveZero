package com.dmitry.pisarevskiy.abovezero;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CityFragment extends Fragment {
    private int id;
    private String nameOfCity;
    private float temp;
    private float speed;
    private int sunrise;
    private int sunset;
    private int dt;
    private int image;
    private int timezone;

    private static final String ARG_ID = "id";
    private static final String ARG_TEMP = "temp";
    private static final String ARG_NAME = "name";
    private static final String ARG_SPEED = "speed";
    private static final String ARG_CLOUDS = "clouds";
    private static final String ARG_SUNRISE = "sunrise";
    private static final String ARG_SUNSET = "sunset";
    private static final String ARG_DT = "dt";
    private static final String ARG_TIMEZONE = "timezone";

    public CityFragment() {
    }

    public static CityFragment newInstance(int id, String name, float temp, float speed, int clouds, int sunrise, int sunset, int dt, int timezone) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putFloat(ARG_TEMP, temp);
        args.putString(ARG_NAME, name);
        args.putFloat(ARG_SPEED, speed);
        args.putInt(ARG_CLOUDS, clouds);
        args.putInt(ARG_SUNRISE, sunrise);
        args.putInt(ARG_SUNSET, sunset);
        args.putInt(ARG_DT, dt);
        args.putInt(ARG_TIMEZONE, timezone);
        fragment.setArguments(args);
        return fragment;
    }

    public int getIndex() {
        if (getArguments() != null) {
            return getArguments().getInt(ARG_ID, 0);
        }
        return 0;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_ID, id);
        outState.putFloat(ARG_TEMP, temp);
        outState.putString(ARG_NAME, nameOfCity);
        outState.putFloat(ARG_SPEED, speed);
        outState.putInt(ARG_CLOUDS, image);
        outState.putInt(ARG_SUNRISE, sunrise);
        outState.putInt(ARG_SUNSET, sunset);
        outState.putInt(ARG_DT, dt);
        outState.putInt(ARG_TIMEZONE, timezone);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
            temp = getArguments().getFloat(ARG_TEMP);
            nameOfCity = getArguments().getString(ARG_NAME);
            speed = getArguments().getFloat(ARG_SPEED);
            image = getArguments().getInt(ARG_CLOUDS);
            sunrise = getArguments().getInt(ARG_SUNRISE);
            sunset = getArguments().getInt(ARG_SUNSET);
            dt = getArguments().getInt(ARG_DT);
            timezone = getArguments().getInt(ARG_TIMEZONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_city, container, false);
        Button btnInfo = layout.findViewById(R.id.btnInfo);
        ImageView imageView = layout.findViewById(R.id.imageView);
        TextView tvTemp = layout.findViewById(R.id.tvTemp);
        TextView tvWindSpeed = layout.findViewById(R.id.tvWindSpeed);
        SunriseSunsetView ssvSunRiseSet = layout.findViewById(R.id.ssvSunRiseSet);

        imageView.setImageResource(image);
        tvTemp.setText((String.format("%.0f", temp + MainActivity.CONSTANT_FOR_KELVIN_SCALE) + MainActivity.degreeUnit));
        tvWindSpeed.setText((String.format("%.1f", speed * MainActivity.windMultiplier) + MainActivity.windUnit));
        ssvSunRiseSet.setSunrise(sunrise + timezone);
        ssvSunRiseSet.setSunset(sunset + timezone);
        ssvSunRiseSet.setDt(dt + timezone);

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = String.format("https://yandex.ru/search/?text=%s город", nameOfCity);
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                CityFragment.this.startActivity(browser);
            }
        });

        return layout;
    }
}