package com.skripsi.mtrtamalate.models.sampah;

import java.util.List;

public class ResponSampah {

    private String kode;
    private String pesan;
    private Sampah result_data;
    private String total_berat;
    private String total_berat_kelurahan;
    private List<SampahPetugas> result_data_petugas;

    public String getTotal_berat_kelurahan() {
        return total_berat_kelurahan;
    }

    public String getTotal_berat() {
        return total_berat;
    }

    public List<SampahPetugas> getResult_data_petugas() {
        return result_data_petugas;
    }

    public Sampah getResult_data() {
        return result_data;
    }

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }
}
