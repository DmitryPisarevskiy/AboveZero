package com.dmitry.pisarevskiy.abovezero;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dmitry.pisarevskiy.abovezero.database.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapterHistory extends RecyclerView.Adapter<RVAdapterHistory.ViewHolder> {
    private List<Request> forecastRequest;
    private List<Request> searchRequest;

    public void setSearchRequest(List<Request> searchRequest) {
        this.searchRequest = searchRequest;
    }

    public List<Request> getSearchRequest() {
        return searchRequest;
    }

    public void setForecastRequest(List<Request> forecastRequest) {
        this.forecastRequest = forecastRequest;
    }

    public List<Request> getForecastRequest() {
        return forecastRequest;
    }

    public RVAdapterHistory(List<Request> forecastHistory) {
        this.forecastRequest = forecastHistory;
        searchRequest = new ArrayList<>();
        searchRequest.addAll(forecastHistory);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(searchRequest.get(position));
    }

    @Override
    public int getItemCount() {
        return searchRequest.size();
    }

    public void filter(String newText) {
        newText = newText.toLowerCase(Locale.getDefault());
        searchRequest.clear();
        if (newText.length()==0) {
            searchRequest.addAll(forecastRequest);
        } else {
            for (Request fh : forecastRequest) {
                if (fh.city.toLowerCase(Locale.getDefault()).contains(newText)) {
                    searchRequest.add(fh);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCity;
        private final TextView tvTemp;
        private final TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvDate = itemView.findViewById(R.id.tvDate);
        }

        public void bind(Request request) {
            tvCity.setText(request.city);
            tvTemp.setText(String.format("%.1f °C",request.temperature));
            tvDate.setText(request.date);
        }
    }
}
