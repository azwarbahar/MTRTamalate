package com.skripsi.mtrtamalate.models.petugas;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Petugas implements Parcelable {

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

	protected Petugas(Parcel in) {
		usiaPekerja = in.readString();
		kelurahanPekerja = in.readString();
		namaPekerja = in.readString();
		fotoPekerja = in.readString();
		idPekerja = in.readString();
		telponPekerja = in.readString();
		jenisKelaminPekerja = in.readString();
		alamatPekerja = in.readString();
		areaPekerja = in.readString();
		password = in.readString();
		latitudePekerja = in.readString();
		statusKerjaPekerja = in.readString();
		kendaraanPekerja = in.readString();
		statusPekerja = in.readString();
		nikPekerja = in.readString();
		longitudePekerja = in.readString();
		rolePekerja = in.readString();
	}

	public static final Creator<Petugas> CREATOR = new Creator<Petugas>() {
		@Override
		public Petugas createFromParcel(Parcel in) {
			return new Petugas(in);
		}

		@Override
		public Petugas[] newArray(int size) {
			return new Petugas[size];
		}
	};

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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(usiaPekerja);
		parcel.writeString(kelurahanPekerja);
		parcel.writeString(namaPekerja);
		parcel.writeString(fotoPekerja);
		parcel.writeString(idPekerja);
		parcel.writeString(telponPekerja);
		parcel.writeString(jenisKelaminPekerja);
		parcel.writeString(alamatPekerja);
		parcel.writeString(areaPekerja);
		parcel.writeString(password);
		parcel.writeString(latitudePekerja);
		parcel.writeString(statusKerjaPekerja);
		parcel.writeString(kendaraanPekerja);
		parcel.writeString(statusPekerja);
		parcel.writeString(nikPekerja);
		parcel.writeString(longitudePekerja);
		parcel.writeString(rolePekerja);
	}
}