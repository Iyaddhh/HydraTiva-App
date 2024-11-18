package com.example.hydrativa.models;

import java.util.Objects;

public class ImageItem {
    private final String id;
    private final int imageId;
    private final String url;

    public ImageItem(String id, int imageResId, String url) {
        this.id = id;
        this.imageId = imageResId;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public int getImageId() {
        return imageId;
    }

    public String getUrl() { return url; }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ImageItem imageItem = (ImageItem) obj;
        return id.equals(imageItem.id) &&
                imageId == imageItem.imageId &&
                Objects.equals(url, imageItem.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageId);
    }
}