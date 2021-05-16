package com.skripsi.mtrtamalate.models.area;

import com.google.gson.annotations.SerializedName;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;

public class ResponseArea {

    @SerializedName("kode")
    private int kode;

    @SerializedName("pesan")
    private String pesan;

    @SerializedName("result_area")
    private Area result_area;

    public int getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public Area getResult_area() {
        return result_area;
    }
}
