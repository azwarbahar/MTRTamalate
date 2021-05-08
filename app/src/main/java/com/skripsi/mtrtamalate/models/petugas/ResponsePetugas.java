package com.skripsi.mtrtamalate.models.petugas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponsePetugas {

    @SerializedName("result_petugas")
    private Petugas petugas;

    @SerializedName("petugas_data")
    private List<Petugas> petugas_data;

    @SerializedName("kode")
    private String kode;

    @SerializedName("pesan")
    private String pesan;

    public List<Petugas> getPetugas_data() {
        return petugas_data;
    }

    public String getPesan() {
        return pesan;
    }

    public Petugas getPetugas() {
        return petugas;
    }

    public String getKode() {
        return kode;
    }
}