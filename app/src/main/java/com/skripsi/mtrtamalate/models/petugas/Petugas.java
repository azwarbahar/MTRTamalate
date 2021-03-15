package com.skripsi.mtrtamalate.models.petugas;

import com.google.gson.annotations.SerializedName;

public class Petugas {

	@SerializedName("usia_pekerja")
	private String usiaPekerja;

	@SerializedName("kelurahan_pekerja")
	private String kelurahanPekerja;

	@SerializedName("nama_pekerja")
	private String namaPekerja;

	@SerializedName("foto_pekerja")
	private String fotoPekerja;

	@SerializedName("id_pekerja")
	private String idPekerja;

	@SerializedName("telpon_pekerja")
	private String telponPekerja;

	@SerializedName("jenis_kelamin_pekerja")
	private String jenisKelaminPekerja;

	@SerializedName("alamat_pekerja")
	private String alamatPekerja;

	@SerializedName("area_pekerja")
	private String areaPekerja;

	@SerializedName("password")
	private String password;

	@SerializedName("latitude_pekerja")
	private String latitudePekerja;

	@SerializedName("status_kerja_pekerja")
	private String statusKerjaPekerja;

	@SerializedName("kendaraan_pekerja")
	private String kendaraanPekerja;

	@SerializedName("status_pekerja")
	private String statusPekerja;

	@SerializedName("nik_pekerja")
	private String nikPekerja;

	@SerializedName("longitude_pekerja")
	private String longitudePekerja;

	@SerializedName("role_pekerja")
	private String rolePekerja;

	public String getRolePekerja() {
		return rolePekerja;
	}

	public String getUsiaPekerja(){
		return usiaPekerja;
	}

	public String getKelurahanPekerja(){
		return kelurahanPekerja;
	}

	public String getNamaPekerja(){
		return namaPekerja;
	}

	public String getFotoPekerja(){
		return fotoPekerja;
	}

	public String getIdPekerja(){
		return idPekerja;
	}

	public String getTelponPekerja(){
		return telponPekerja;
	}

	public String getJenisKelaminPekerja(){
		return jenisKelaminPekerja;
	}

	public String getAlamatPekerja(){
		return alamatPekerja;
	}

	public String getAreaPekerja(){
		return areaPekerja;
	}

	public String getPassword(){
		return password;
	}

	public String getLatitudePekerja(){
		return latitudePekerja;
	}

	public String getStatusKerjaPekerja(){
		return statusKerjaPekerja;
	}

	public String getKendaraanPekerja(){
		return kendaraanPekerja;
	}

	public String getStatusPekerja(){
		return statusPekerja;
	}

	public String getNikPekerja(){
		return nikPekerja;
	}

	public String getLongitudePekerja(){
		return longitudePekerja;
	}
}