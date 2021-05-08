package com.skripsi.mtrtamalate.models.kendaraan;

import com.google.gson.annotations.SerializedName;

public class Kendaraan {

	@SerializedName("kode_kendaraan")
	private String kodeKendaraan;

	@SerializedName("kondisi_kendaraan")
	private String kondisiKendaraan;

	@SerializedName("foto_kendaraan")
	private String fotoKendaraan;

	@SerializedName("kelurahan_kendaraan")
	private String kelurahanKendaraan;

	@SerializedName("nomor_kendaraan")
	private String nomorKendaraan;

	@SerializedName("nama_kendaraan")
	private String namaKendaraan;

	@SerializedName("type_kendaraan")
	private String typeKendaraan;

	@SerializedName("status_kendaraan")
	private String statusKendaraan;

	@SerializedName("id_kendaraan")
	private String idKendaraan;

	public String getKodeKendaraan(){
		return kodeKendaraan;
	}

	public String getKondisiKendaraan(){
		return kondisiKendaraan;
	}

	public String getFotoKendaraan(){
		return fotoKendaraan;
	}

	public String getKelurahanKendaraan(){
		return kelurahanKendaraan;
	}

	public String getNomorKendaraan(){
		return nomorKendaraan;
	}

	public String getNamaKendaraan(){
		return namaKendaraan;
	}

	public String getTypeKendaraan(){
		return typeKendaraan;
	}

	public String getStatusKendaraan(){
		return statusKendaraan;
	}

	public String getIdKendaraan(){
		return idKendaraan;
	}
}