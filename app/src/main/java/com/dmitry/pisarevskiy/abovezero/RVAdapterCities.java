package com.dmitry.pisarevskiy.abovezero;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapterCities extends RecyclerView.Adapter<RVAdapterCities.ViewHolder> {
    private List<String> cities;

    public RVAdapterCities(List<String> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new RVAdapterCities.ViewHolder(
//                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false)
//        );
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvCity = view.findViewById(R.id.tvCity);
                CityActivity.etCity.setText(tvCity.getText());
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(cities.get(position));
    }

    @Override
    public int getItemCount() {
        if (cities == null) {
            return 0;
        };
        return cities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
        }

        void bind(String city) {
            tvCity.setText(city);
        }
    }
}
