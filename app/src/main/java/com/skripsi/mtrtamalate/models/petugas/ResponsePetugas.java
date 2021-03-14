package com.skripsi.mtrtamalate.models.petugas;

import com.google.gson.annotations.SerializedName;

public class ResponsePetugas {

    @SerializedName("result_petugas")
    private Petugas petugas;

    @SerializedName("kode")
    private String kode;

    @SerializedName("pesan")
    private String pesan;

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