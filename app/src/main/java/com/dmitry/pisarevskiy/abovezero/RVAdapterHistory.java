package com.dmitry.pisarevskiy.abovezero;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitry.pisarevskiy.abovezero.weather.ForecastWeather;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapterHistory extends RecyclerView.Adapter<RVAdapterHistory.ViewHolder> {
    private ArrayList<ForecastWeather> forecastHistory;
    private ArrayList<ForecastWeather> searchHistory;

    public void setSearchHistory(ArrayList<ForecastWeather> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public ArrayList<ForecastWeather> getSearchHistory() {
        return searchHistory;
    }

    public void setForecastHistory(ArrayList<ForecastWeather> forecastHistory) {
        this.forecastHistory = forecastHistory;
    }

    public ArrayList<ForecastWeather> getForecastHistory() {
        return forecastHistory;
    }

    public RVAdapterHistory(ArrayList<ForecastWeather> forecastHistory) {
        this.forecastHistory = forecastHistory;
        searchHistory = new ArrayList<>();
        searchHistory.addAll(forecastHistory);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(searchHistory.get(position));
    }

    @Override
    public int getItemCount() {
        return searchHistory.size();
    }

    public void filter(String newText) {
        newText = newText.toLowerCase(Locale.getDefault());
        searchHistory.clear();
        if (newText.length()==0) {
            searchHistory.addAll(forecastHistory);
        } else {
            for (ForecastWeather fh : forecastHistory) {
                if (fh.getCity().getName().toLowerCase(Locale.getDefault()).contains(newText)) {
                    searchHistory.add(fh);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCity;
        private final TextView tvRequest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvRequest = itemView.findViewById(R.id.tvRequest);
        }

        public void bind(ForecastWeather forecastWeather) {
            tvCity.setText(forecastWeather.getCity().getName());
            tvRequest.setText(forecastWeather.getRequest());
        }
    }
}
