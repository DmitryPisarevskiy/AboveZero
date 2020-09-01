package com.dmitry.pisarevskiy.abovezero;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private List<String> temperatures;
    private List<String> pressures;
    private List<String> winds;

    public RVAdapter(List<String> temperatures, List<String> pressures, List<String> winds) {
        this.temperatures = temperatures;
        this.pressures = pressures;
        this.winds = winds;
    }

    public void setTemperatures(List<String> temperatures) {
        this.temperatures = temperatures;
        notifyDataSetChanged();
    }

    public void setPressures(List<String> pressures) {
        this.pressures = pressures;
        notifyDataSetChanged();
    }

    public void setWinds(List<String> winds) {
        this.winds = winds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(temperatures.get(position),winds.get(position),pressures.get(position));
    }

    @Override
    public int getItemCount() {
        if (temperatures==null) {
            return 0;
        };
        return temperatures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTemperature;
        private final TextView tvWind;
        private final TextView tvPressure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvWind = itemView.findViewById(R.id.tvWind);
            tvPressure = itemView.findViewById(R.id.tvPressure);
        }

        void bind(String temperature, String wind, String pressure) {
            tvTemperature.setText(temperature);
            tvWind.setText(wind);
            tvPressure.setText(pressure);
        }
    }
}
