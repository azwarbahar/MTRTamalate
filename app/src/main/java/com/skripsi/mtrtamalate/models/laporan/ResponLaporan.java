package com.skripsi.mtrtamalate.models.laporan;

import java.util.List;

public class ResponLaporan {

    private String kode;
    private String pesan;
    private List<Laporan> result_laporan_masyarakat;
    private List<Laporan> result_marker_laporan;

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
