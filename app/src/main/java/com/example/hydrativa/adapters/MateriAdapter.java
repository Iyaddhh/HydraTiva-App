package com.example.hydrativa.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hydrativa.Materi;
import com.example.hydrativa.models.MateriResponse;
import com.example.hydrativa.R;
import com.example.hydrativa.retrofit.MateriService;

import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder> {
    private List<MateriResponse> materiList;
    private Context context;
    private MateriService materiService;

    public MateriAdapter(List<MateriResponse> materiList, Context context, MateriService materiService) {
        this.materiList = materiList;
        this.context = context;
        this.materiService = materiService;
    }

    public void updateList(List<MateriResponse> newMateriList) {
        this.materiList.clear();
        this.materiList.addAll(newMateriList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout, assuming you are using a grid
        View view = LayoutInflater.from(context).inflate(R.layout.activity_materi_card, parent, false);
        return new MateriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        MateriResponse materi = materiList.get(position);

        // Set the text for the title and description
        holder.judul.setText(materi.getJudul());
        holder.deskripsi.setText(materi.getDeskripsi());

        // Load image using Glide
        Glide.with(context)
                .load(materi.getGambar())
                .placeholder(R.drawable.load_image)
                .into(holder.gambar);

        // Set the onClickListener for the grid item
        holder.gridKonten.setOnClickListener(v -> {
            // Pass the materi_id as an intent extra
            Intent intent = new Intent(context, Materi.class);
            intent.putExtra("materi_id", materi.getId());
            Log.d(TAG, "onBindViewHolder: Materi ID " + materi.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    // ViewHolder class to hold the item views
    public static class MateriViewHolder extends RecyclerView.ViewHolder {
        TextView judul, deskripsi;
        ImageView gambar;
        View gridKonten; // The clickable view for each grid item

        public MateriViewHolder(@NonNull View itemView) {
            super(itemView);
            judul = itemView.findViewById(R.id.judulMateri);
            deskripsi = itemView.findViewById(R.id.isiMateri);
            gambar = itemView.findViewById(R.id.imageMateri);
            gridKonten = itemView.findViewById(R.id.gridKonten); // Initialize the clickable grid item
        }
    }
}
