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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CityFragment extends Fragment {
    int currentPosition;
    String nameOfCity;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INDEX = "param1";
    private static final String ARG_NAME = "param2";

    public CityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CityFragment newInstance(int param1, String param2) {
        CityFragment fragment = new CityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, param1);
        args.putString(ARG_NAME, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public int getIndex() {
        if (getArguments() != null) {
            return getArguments().getInt(ARG_INDEX,0);
        }
        return 0;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_INDEX, currentPosition);
        outState.putString(ARG_NAME, nameOfCity);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPosition = getArguments().getInt(ARG_INDEX);
            nameOfCity = getArguments().getString(ARG_NAME);
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
        TypedArray imgs = getResources().obtainTypedArray(R.array.cities_image);
        TypedArray temps = getResources().obtainTypedArray(R.array.cities_temp);
        TypedArray winds = getResources().obtainTypedArray(R.array.cities_wind);

        imageView.setImageResource(imgs.getResourceId(getIndex(), -1));
        tvTemp.setText((temps.getString(getIndex())+MainActivity.degreeUnit));
        tvWindSpeed.setText((winds.getString(getIndex())+MainActivity.windUnit));

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