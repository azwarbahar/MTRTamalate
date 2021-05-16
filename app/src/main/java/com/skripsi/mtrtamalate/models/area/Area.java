package com.skripsi.mtrtamalate.models.area;

import com.google.gson.annotations.SerializedName;

public class Area {

    @SerializedName("id_area")
    private String id_area;

    @SerializedName("nama_area")
    private String nama_area;

    @SerializedName("kelurahan_area")
    private String kelurahan_area;

    public String getId_area() {
        return id_area;
    }

    public String getNama_area() {
        return nama_area;
    }

    public String getKelurahan_area() {
        return kelurahan_area;
    }
}
