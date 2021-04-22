package com.skripsi.mtrtamalate.models.masyarakat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMasyarakat {

    @SerializedName("kode")
    private int kode;

    @SerializedName("pesan")
    private String pesan;

    @SerializedName("auth_masayarkat")
    private Masayarkat masayarkat;

    @SerializedName("result_masyarakat")
    private Masayarkat result_masyarakat;

    @SerializedName("masyarakat_marker")
    private List<Masayarkat> masyarakat_marker;

    @SerializedName("masyarakat_data")
    private List<Masayarkat> masyarakat_data;

    public List<Masayarkat> getMasyarakat_data() {
        return masyarakat_data;
    }

    public List<Masayarkat> getMasyarakat_marker() {
        return masyarakat_marker;
    }

    public Masayarkat getResult_masyarakat() {
        return result_masyarakat;
    }

    public String getPesan() {
        return pesan;
    }

    public int getKode() {
        return kode;
    }

    public Masayarkat getMasayarkat() {
        return masayarkat;
    }
}