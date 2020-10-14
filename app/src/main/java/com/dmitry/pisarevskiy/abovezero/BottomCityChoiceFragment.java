package com.dmitry.pisarevskiy.abovezero;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BottomCityChoiceFragment extends BottomSheetDialogFragment {
    protected static TextInputEditText tietCity;
    private Pattern checkCityName;
    private View fragment;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.fragment_new_city,container,false);
        setCancelable(true);
        checkCityName= Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}$");
        fragment.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tietCity.getText()!=null) {
                    if (checkCityName.matcher(tietCity.getText().toString()).matches()) {
                        ((ArrayAdapter<String>)((Spinner) getActivity().findViewById(R.id.spCity)).getAdapter()).add(tietCity.getText().toString());
                        dismiss();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                        builder.setTitle(R.string.incorrect_name)
                                .setIcon(R.drawable.ic_baseline_error_outline_24)
                                .setPositiveButton("Вернуться на главный экран", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dismiss();
                                    }
                                })
                                .setNegativeButton("Ввести другое название", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tietCity.setText("");
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });
        fragment.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tietCity = fragment.findViewById(R.id.tietCity);
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
        RecyclerView rvCities = fragment.findViewById(R.id.rvCities);
        RVAdapterCities rvAdapterCities = new RVAdapterCities(Arrays.asList(getResources().getStringArray(R.array.cities)));
        rvCities.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
        rvCities.setAdapter(rvAdapterCities);

        return fragment;
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
