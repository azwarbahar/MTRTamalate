package com.skripsi.mtrtamalate.models.berita;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseBerita{

	@SerializedName("kode")
	private int kode;

	@SerializedName("berita")
	private List<Berita> berita;

	public int getKode(){
		return kode;
	}

	public List<Berita> getBerita(){
		return berita;
	}
}