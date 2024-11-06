package com.example.hydrativa.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydrativa.detail_watering;
import com.example.hydrativa.R;
import com.example.hydrativa.models.Kebun;
import com.example.hydrativa.retrofit.KebunService;
import com.zerobranch.layout.SwipeLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KebunAdapter extends RecyclerView.Adapter<KebunAdapter.KebunViewHolder> {

    private List<Kebun> kebunList;
    private Context context;
    private KebunService kebunService;

    public KebunAdapter(Context context, List<Kebun> kebunList, KebunService kebunService) {
        this.context = context;
        this.kebunList = kebunList;
        this.kebunService = kebunService;
    }

    @NonNull
    @Override
    public KebunViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.kebun_view, parent, false);
        return new KebunViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KebunViewHolder holder, int position) {
        Kebun kebun = kebunList.get(position);
        holder.title.setText(kebun.getNama_kebun());
        holder.location.setText(kebun.getLokasi_kebun());

        // Menangani klik pada ikon delete
        holder.deleteIcon.setOnClickListener(v -> deleteKebun(kebun.getKebun_id(), position));

        // Menangani klik pada arrowRight1 untuk berpindah ke detail_watering
        holder.arrowRight1.setOnClickListener(v -> {
            Intent intent = new Intent(context, detail_watering.class);
            intent.putExtra("KEBUN_ID", kebun.getKebun_id());
            intent.putExtra("KEBUN_TITLE", kebun.getNama_kebun());
            intent.putExtra("KEBUN_LOCATION", kebun.getLokasi_kebun());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return kebunList.size();
    }

    private void deleteKebun(int kebun_id, int position) {
        Call<Void> call = kebunService.deleteKebun(kebun_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    kebunList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Kebun berhasil dihapus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Gagal menghapus kebun", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class KebunViewHolder extends RecyclerView.ViewHolder {
        TextView title, location;
        ImageView deleteIcon, arrowRight1;
        SwipeLayout swipeLayout;

        public KebunViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.kebunTitle1);
            location = itemView.findViewById(R.id.kebunLocation1);
            deleteIcon = itemView.findViewById(R.id.right_view);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);
            arrowRight1 = itemView.findViewById(R.id.arrowRight1); // Tambahkan ini
        }
    }
}
