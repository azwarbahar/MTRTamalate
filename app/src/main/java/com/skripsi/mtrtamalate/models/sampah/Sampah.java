package com.skripsi.mtrtamalate.models.sampah;

import com.google.gson.annotations.SerializedName;

public class Sampah{

	@SerializedName("id_sampah")
	private String idSampah;

	@SerializedName("petugas_id")
	private String petugasId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("jumlah_sampah")
	private String jumlahSampah;

	@SerializedName("tanggal_manual")
	private String tanggalManual;

	@SerializedName("satuan_sampah")
	private String satuanSampah;

	@SerializedName("kelurahan_sampah")
	private String kelurahanSampah;

	public String getIdSampah(){
		return idSampah;
	}

	public String getPetugasId(){
		return petugasId;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getJumlahSampah(){
		return jumlahSampah;
	}

	public String getTanggalManual(){
		return tanggalManual;
	}

	public String getSatuanSampah(){
		return satuanSampah;
	}

	public String getKelurahanSampah(){
		return kelurahanSampah;
	}
}