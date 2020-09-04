package com.dmitry.pisarevskiy.abovezero;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapterData extends RecyclerView.Adapter<RVAdapterData.ViewHolder> {
    private List<String> times;
    private int[] images;
    private List<String> temperatures;
    private List<String> pressures;
    private List<String> winds;


    public void setTimes(List<String> times) {
        this.times = times;
        notifyDataSetChanged();
    }

    public void setImages(int[] images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public RVAdapterData(List<String> times, int[] images, List<String> temperatures, List<String> pressures, List<String> winds) {
        this.times = times;
        this.images = images;
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
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(times.get(position),  images[position], temperatures.get(position), winds.get(position), pressures.get(position));
    }

    @Override
    public int getItemCount() {
        if (temperatures == null) {
            return 0;
        };
        return temperatures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTime;
        private final ImageView imgCloudiness;
        private final TextView tvTemperature;
        private final TextView tvWind;
        private final TextView tvPressure;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            imgCloudiness = itemView.findViewById(R.id.imgCloudiness);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvWind = itemView.findViewById(R.id.tvWind);
            tvPressure = itemView.findViewById(R.id.tvPressure);
            if (!MainActivity.showWind) {
                tvWind.setVisibility(View.GONE);
            } else {
                tvWind.setVisibility(View.VISIBLE);
            }
            if (!MainActivity.showPressure) {
                tvPressure.setVisibility(View.GONE);
            } else {
                tvPressure.setVisibility(View.VISIBLE);
            }
        }

        void bind(String time, int image, String temperature, String wind, String pressure) {
            tvTime.setText(time);
            imgCloudiness.setImageResource(image);
            tvTemperature.setText((temperature + MainActivity.degreeUnit));
            tvWind.setText((wind + MainActivity.windUnit));
            tvPressure.setText((pressure + MainActivity.pressureUnit));
        }
    }
}
