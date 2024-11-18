package com.example.hydrativa.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.hydrativa.R;
import com.example.hydrativa.models.ImageItem;

public class ImageAdapter extends ListAdapter<ImageItem, ImageAdapter.ViewHolder> {

    public ImageAdapter() {
        super(new DiffCallback());
    }

    public static class DiffCallback extends DiffUtil.ItemCallback<ImageItem> {
        @Override
        public boolean areItemsTheSame(@NonNull ImageItem oldItem, @NonNull ImageItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ImageItem oldItem, @NonNull ImageItem newItem) {
            return oldItem.getImageId() == newItem.getImageId();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bindData(ImageItem item) {
            Glide.with(itemView)
                    .load(item.getImageId())
                    .into(imageView);

            imageView.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                v.getContext().startActivity(browserIntent);
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageItem imageItem = getItem(position);
        holder.bindData(imageItem);
    }
}