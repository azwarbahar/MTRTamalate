package com.skripsi.mtrtamalate.models.sampah;

import com.google.gson.annotations.SerializedName;

public class SampahPetugas{

	@SerializedName("longitude_sampah")
	private String longitudeSampah;

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("id_petugas")
	private String idPetugas;

	@SerializedName("berat_sampah")
	private String beratSampah;

	@SerializedName("kelurahan")
	private String kelurahan;

	@SerializedName("foto_bukti")
	private String fotoBukti;

	@SerializedName("crated_at")
	private String cratedAt;

	@SerializedName("update_at")
	private String updateAt;

	@SerializedName("latitude_sampah")
	private String latitudeSampah;

	@SerializedName("tanggal")
	private String tanggal;

	@SerializedName("alamat_sampah")
	private String alamatSampah;

	@SerializedName("id_laporan_petugas")
	private String idLaporanPetugas;

	@SerializedName("id_koordinator")
	private String idKoordinator;

	public String getLongitudeSampah(){
		return longitudeSampah;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public String getIdPetugas(){
		return idPetugas;
	}

	public String getBeratSampah(){
		return beratSampah;
	}

	public String getKelurahan(){
		return kelurahan;
	}

	public String getFotoBukti(){
		return fotoBukti;
	}

	public String getCratedAt(){
		return cratedAt;
	}

	public String getUpdateAt(){
		return updateAt;
	}

	public String getLatitudeSampah(){
		return latitudeSampah;
	}

	public String getTanggal(){
		return tanggal;
	}

	public String getAlamatSampah(){
		return alamatSampah;
	}

	public String getIdLaporanPetugas(){
		return idLaporanPetugas;
	}

	public String getIdKoordinator(){
		return idKoordinator;
	}
}