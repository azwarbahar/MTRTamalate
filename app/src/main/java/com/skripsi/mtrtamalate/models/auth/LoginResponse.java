package com.skripsi.mtrtamalate.models.auth;

import com.google.gson.annotations.SerializedName;
import com.skripsi.mtrtamalate.models.masyarakat.Masayarkat;
import com.skripsi.mtrtamalate.models.petugas.Petugas;

public class LoginResponse {

    @SerializedName("kode")
    private String kode;

    @SerializedName("role")
    private String role;

    @SerializedName("pesan")
    private String pesan;

    @SerializedName("auth_masayarkat")
    private Masayarkat masayarkat;

    @SerializedName("auth_petugas")
    private Petugas petugas;

    public String getRole() {
        return role;
    }

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }

    public Masayarkat getMasayarkat() {
        return masayarkat;
    }

    public Petugas getPetugas() {
        return petugas;
    }
}
