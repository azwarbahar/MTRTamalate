package com.skripsi.mtrtamalate.models.kendaraan;

import com.google.gson.annotations.SerializedName;

public class ResponseKendaraan{

	@SerializedName("kode")
	private String kode;

	@SerializedName("pesan")
	private String pesan;

	public String getPesan() {
		return pesan;
	}

	@SerializedName("result_kendaraan")
	private Kendaraan kendaraan;

	public String getKode(){
		return kode;
	}

	public Kendaraan getKendaraan(){
		return kendaraan;
	}
}