package com.skripsi.mtrtamalate.models.laporan;

import java.util.List;

public class ResponLaporan {

    private String kode;
    private String pesan;
    private Laporan result_laporan;
    private List<Laporan> result_laporan_masyarakat;
    private List<Laporan> result_laporan_petugas;
    private List<Laporan> result_marker_laporan;
    private List<Laporan> result_laporan_berjalan;

    public Laporan getResult_laporan() {
        return result_laporan;
    }

    public List<Laporan> getResult_laporan_berjalan() {
        return result_laporan_berjalan;
    }

    public List<Laporan> getResult_laporan_petugas() {
        return result_laporan_petugas;
    }

    public List<Laporan> getResult_marker_laporan() {
        return result_marker_laporan;
    }

    public List<Laporan> getResult_laporan_masyarakat() {
        return result_laporan_masyarakat;
    }

    public String getKode() {
        return kode;
    }

    public String getPesan() {
        return pesan;
    }
}
