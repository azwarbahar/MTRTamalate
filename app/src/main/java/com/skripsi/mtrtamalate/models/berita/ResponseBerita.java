package com.skripsi.mtrtamalate.models.berita;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBerita{

	@SerializedName("kode")
	private int kode;

	@SerializedName("pesan")
	private String pesan;

	@SerializedName("berita")
	private List<Berita> berita;

	public String getPesan() {
		return pesan;
	}

	public int getKode(){
		return kode;
	}

	public List<Berita> getBerita(){
		return berita;
	}
}