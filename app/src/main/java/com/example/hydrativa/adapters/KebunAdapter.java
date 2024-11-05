package com.example.hydrativa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hydrativa.R;
import com.example.hydrativa.models.Kebun;

import java.util.List;

public class KebunAdapter extends RecyclerView.Adapter<KebunAdapter.KebunViewHolder> {

    List<Kebun> kebunList;
    Context context;


    public KebunAdapter(Context context, List<Kebun> kebun){
        this.context = context;
        kebunList = kebun;
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
        holder.title.setText(kebun.getNamaKebun());
        holder.title.setText(kebun.getLokasiKebun());
    }

    @Override
    public int getItemCount() {
        return kebunList.size();
    }

    public class KebunViewHolder extends RecyclerView.ViewHolder{
        TextView title, location;
        public KebunViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.kebunTitle1);
            location = itemView.findViewById(R.id.kebunLocation1);
        }
    }
}
