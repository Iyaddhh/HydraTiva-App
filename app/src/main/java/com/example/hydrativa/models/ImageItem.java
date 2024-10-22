package com.example.hydrativa.models;

import java.util.Objects;

public class ImageItem {
    private final String id;
    private final int imageId;

    public ImageItem(String id, int imageResId) {
        this.id = id;
        this.imageId = imageResId;
    }

    public String getId() {
        return id;
    }

    public int getImageId() {
        return imageId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ImageItem imageItem = (ImageItem) obj;
        return id.equals(imageItem.id) && imageId == imageItem.imageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageId);
    }
}