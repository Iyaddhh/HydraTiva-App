package com.example.hydrativa.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hydrativa.R;
import com.example.hydrativa.models.HistoryPenyiraman;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryPenyiramanAdapter extends RecyclerView.Adapter<HistoryPenyiramanAdapter.HistoryViewHolder> {

    private List<HistoryPenyiraman> historyList;
    private Map<String, HistoryPenyiraman> latestHistoryByDate;

    public HistoryPenyiramanAdapter(List<HistoryPenyiraman> historyList) {
        this.historyList = historyList;
        this.latestHistoryByDate = new HashMap<>();
        filterLatestData();
    }

    private void filterLatestData() {
        Collections.sort(historyList, (h1, h2) -> {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date1 = inputFormat.parse(h1.getDate());
                Date date2 = inputFormat.parse(h2.getDate());

                if (date1 != null && date2 != null) {
                    return date2.compareTo(date1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });

        for (HistoryPenyiraman history : historyList) {
            String date = history.getDate().split(" ")[0];
            if (!latestHistoryByDate.containsKey(date)) {
                latestHistoryByDate.put(date, history);
            }
        }

        historyList.clear();
        historyList.addAll(latestHistoryByDate.values());
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_card, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryPenyiraman history = historyList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = null;

        try {
            if (history.getDate() != null) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = inputFormat.parse(history.getDate());

                if (date != null) {
                    formattedDate = dateFormat.format(date);
                } else {
                    formattedDate = "Invalid Date Format";
                }
            } else {
                formattedDate = "No Date";
            }
        } catch (Exception e) {
            formattedDate = "Invalid Date Format";
        }

        Log.d("HistoryPenyiraman", "Date from server: " + history.getDate());

        holder.dateText.setText(formattedDate != null ? formattedDate : "No Date");
        holder.moistureStatus.setText(history.getMoisture() != null ? history.getMoisture() : "No Moisture Status");
        holder.statusValue.setText(history.getSoilCondition() != null ? history.getSoilCondition() : "No Soil Condition");
        holder.soilPhValue.setText(history.getPhLevel() != null ? history.getPhLevel() : "No pH Level");
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView dateText, moistureStatus, statusValue, soilPhValue;

        public HistoryViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.dateText);
            moistureStatus = view.findViewById(R.id.moistureStatus);
            statusValue = view.findViewById(R.id.statusValue);
            soilPhValue = view.findViewById(R.id.soilPhValue);
        }
    }
}
