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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HistoryPenyiramanAdapter extends RecyclerView.Adapter<HistoryPenyiramanAdapter.HistoryViewHolder> {

    private List<HistoryPenyiraman> historyList;
    private Set<String> displayedDates; // Set untuk melacak tanggal yang sudah ditampilkan

    public HistoryPenyiramanAdapter(List<HistoryPenyiraman> historyList) {
        this.historyList = historyList;
        this.displayedDates = new HashSet<>(); // Menggunakan HashSet untuk menghindari duplikasi
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_card, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryPenyiraman history = historyList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd"); // Format yang diinginkan
        String formattedDate = null;

        try {
            if (history.getDate() != null) {
                // Format input sesuai dengan format tanggal yang diterima dari server
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = inputFormat.parse(history.getDate()); // Parsing tanggal dari server

                if (date != null) {
                    formattedDate = dateFormat.format(date); // Menformat hanya tanggalnya
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

        // Cek apakah tanggal sudah ditampilkan
        if (!displayedDates.contains(formattedDate)) {
            // Jika tanggal belum ada, tampilkan item dan tambahkan tanggal ke dalam set
            holder.dateText.setText(formattedDate != null ? formattedDate : "No Date");
            holder.moistureStatus.setText(history.getMoisture() != null ? history.getMoisture() : "No Moisture Status");
            holder.statusValue.setText(history.getSoilCondition() != null ? history.getSoilCondition() : "No Soil Condition");
            holder.soilPhValue.setText(history.getPhLevel() != null ? history.getPhLevel() : "No pH Level");

            // Tambahkan tanggal ke set untuk memastikan tidak ada duplikasi
            displayedDates.add(formattedDate);
        } else {
            // Jika tanggal sudah ditampilkan, sembunyikan CardView
            holder.itemView.setVisibility(View.GONE);
        }
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
