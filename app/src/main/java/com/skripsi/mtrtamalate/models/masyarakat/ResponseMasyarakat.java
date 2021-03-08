package com.skripsi.mtrtamalate.models.masyarakat;

import com.google.gson.annotations.SerializedName;

public class ResponseMasyarakat{

	@SerializedName("kode")
	private int kode;

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("auth_masayarkat")
	private Masayarkat masayarkat;

	@SerializedName("result_masyarakat")
	private Masayarkat result_masyarakat;

	public Masayarkat getResult_masyarakat() {
		return result_masyarakat;
	}

	public String getPesan() {
		return pesan;
	}

	public int getKode(){
		return kode;
	}

	public Masayarkat getMasayarkat(){
		return masayarkat;
	}
}