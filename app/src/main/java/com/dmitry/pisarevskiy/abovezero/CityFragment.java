package com.dmitry.pisarevskiy.abovezero;

import android.content.Intent;
import android.content.res.TypedArray;
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
    private int speed;
    private int clouds;

    private static final String ARG_ID = "id";
    private static final String ARG_TEMP = "temp";
    private static final String ARG_NAME = "name";
    private static final String ARG_SPEED = "speed";
    private static final String ARG_CLOUDS = "clouds";

    public CityFragment() {}

    public static CityFragment newInstance(int id, String name, float temp, int speed, int clouds) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putFloat(ARG_ID, id);
        args.putFloat(ARG_TEMP, temp);
        args.putString(ARG_NAME, name);
        args.putInt(ARG_SPEED, speed);
        args.putInt(ARG_CLOUDS, clouds);
        fragment.setArguments(args);
        return fragment;
    }

    public int getIndex() {
        if (getArguments() != null) {
            return getArguments().getInt(ARG_ID,0);
        }
        return 0;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_ID, id);
        outState.putFloat(ARG_TEMP, temp);
        outState.putString(ARG_NAME, nameOfCity);
        outState.putInt(ARG_SPEED, speed);
        outState.putInt(ARG_CLOUDS, clouds);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
            temp = getArguments().getFloat(ARG_TEMP);
            nameOfCity = getArguments().getString(ARG_NAME);
            speed = getArguments().getInt(ARG_SPEED);
            clouds = getArguments().getInt(ARG_CLOUDS);
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

        if (clouds<=25) {
            imageView.setImageResource(R.drawable.sunny);
        } else if (clouds<=50) {
            imageView.setImageResource(R.drawable.week_cloudly);
        } else {
            imageView.setImageResource(R.drawable.cloudly);
        }
        tvTemp.setText((String.valueOf(temp)+MainActivity.degreeUnit));
        tvWindSpeed.setText((String.valueOf(speed)+MainActivity.windUnit));

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = String.format("https://yandex.ru/search/?text=%s погода на неделю", nameOfCity);
                Uri uri = Uri.parse(url);
                Intent browser = new Intent(Intent.ACTION_VIEW, uri);
                CityFragment.this.startActivity(browser);
            }
        });

        return layout;
    }
}