package com.skripsi.mtrtamalate.models.berita;

import com.google.gson.annotations.SerializedName;

public class Berita {

	@SerializedName("penulis_berita")
	private String penulisBerita;

	@SerializedName("update_at")
	private String updateAt;

	@SerializedName("foto_berita")
	private String fotoBerita;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id_berita")
	private String idBerita;

	@SerializedName("judul_berita")
	private String judulBerita;

	@SerializedName("isi_berita")
	private String isiBerita;

	public String getPenulisBerita(){
		return penulisBerita;
	}

	public String getUpdateAt(){
		return updateAt;
	}

	public String getFotoBerita(){
		return fotoBerita;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getIdBerita(){
		return idBerita;
	}

	public String getJudulBerita(){
		return judulBerita;
	}

	public String getIsiBerita(){
		return isiBerita;
	}
}