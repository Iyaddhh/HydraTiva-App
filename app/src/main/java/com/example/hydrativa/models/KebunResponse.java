package com.example.hydrativa.models;

import java.util.List;

public class KebunResponse {
    private List<Kebun> data;

    public KebunResponse(List<Kebun> data) {
        this.data = data;
    }

    public List<Kebun> getData() {
        return data;
    }

    public void setData(List<Kebun> data) {
        this.data = data;
    }
}
