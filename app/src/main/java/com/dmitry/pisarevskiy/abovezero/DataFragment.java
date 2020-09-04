package com.dmitry.pisarevskiy.abovezero;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;


public class DataFragment extends Fragment {

    private static final String ARG_TIMES = "Time";
    private static final String ARG_CLOUDINESS = "Cloudiness";
    private static final String ARG_TEMPERATURES = "Temperatures";
    private static final String ARG_PRESSURES = "Pressures";
    private static final String ARG_WINDS = "Winds";

    private String[] times;
    private int[] images;
    private String[] temperatures;
    private String[] pressures;
    private String[] winds;
//    private TypedArray cloudiness;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance(String[] times, int[] images, String[] temperatures, String[] pressures, String[] winds) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_TIMES, times);
        args.putIntArray(ARG_CLOUDINESS, images);
        args.putStringArray(ARG_TEMPERATURES, temperatures);
        args.putStringArray(ARG_PRESSURES, pressures);
        args.putStringArray(ARG_WINDS, winds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            times =getArguments().getStringArray(ARG_TIMES);
            images = getArguments().getIntArray(ARG_CLOUDINESS);
            temperatures = getArguments().getStringArray(ARG_TEMPERATURES);
            pressures = getArguments().getStringArray(ARG_PRESSURES);
            winds = getArguments().getStringArray(ARG_WINDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_data, container, false);
        final RecyclerView rvData = layout.findViewById(R.id.rvData);
        rvData.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL,false));
        final RVAdapterData rvAdapter = new RVAdapterData(
                Arrays.asList(times),
                images,
                Arrays.asList(temperatures),
                Arrays.asList(pressures),
                Arrays.asList(winds));
        rvData.setAdapter(rvAdapter);
        return layout;
    }
}