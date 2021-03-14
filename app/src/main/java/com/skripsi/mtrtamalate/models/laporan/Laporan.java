package com.skripsi.mtrtamalate.models.laporan;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Laporan implements Parcelable {

	@SerializedName("petugas_id")
	private String petugasId;

	@SerializedName("id_laporan")
	private String idLaporan;

	@SerializedName("foto_laporan")
	private String fotoLaporan;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("latitude_laporan")
	private String latitudeLaporan;

	@SerializedName("staus_laporan")
	private String stausLaporan;

	@SerializedName("longitude_laporan")
	private String longitudeLaporan;

	@SerializedName("nik_laporan")
	private String nikLaporan;

	@SerializedName("masyarakat_id")
	private String masyarakatId;

	@SerializedName("kelurahan_laporan")
	private String kelurahanLaporan;

	@SerializedName("keterangan_laporan")
	private String keteranganLaporan;

	@SerializedName("update_at")
	private String updateAt;

	@SerializedName("alamat_laporan")
	private String alamatLaporan;

	@SerializedName("area_laporan")
	private String areaLaporan;

	protected Laporan(Parcel in) {
		petugasId = in.readString();
		idLaporan = in.readString();
		fotoLaporan = in.readString();
		createdAt = in.readString();
		latitudeLaporan = in.readString();
		stausLaporan = in.readString();
		longitudeLaporan = in.readString();
		nikLaporan = in.readString();
		masyarakatId = in.readString();
		kelurahanLaporan = in.readString();
		keteranganLaporan = in.readString();
		updateAt = in.readString();
		alamatLaporan = in.readString();
		areaLaporan = in.readString();
	}

	public static final Creator<Laporan> CREATOR = new Creator<Laporan>() {
		@Override
		public Laporan createFromParcel(Parcel in) {
			return new Laporan(in);
		}

		@Override
		public Laporan[] newArray(int size) {
			return new Laporan[size];
		}
	};

	public String getPetugasId(){
		return petugasId;
	}

	public String getIdLaporan(){
		return idLaporan;
	}

	public String getFotoLaporan(){
		return fotoLaporan;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getLatitudeLaporan(){
		return latitudeLaporan;
	}

	public String getStausLaporan(){
		return stausLaporan;
	}

	public String getLongitudeLaporan(){
		return longitudeLaporan;
	}

	public String getNikLaporan(){
		return nikLaporan;
	}

	public String getMasyarakatId(){
		return masyarakatId;
	}

	public String getKelurahanLaporan(){
		return kelurahanLaporan;
	}

	public String getKeteranganLaporan(){
		return keteranganLaporan;
	}

	public String getUpdateAt(){
		return updateAt;
	}

	public String getAlamatLaporan(){
		return alamatLaporan;
	}

	public String getAreaLaporan(){
		return areaLaporan;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(petugasId);
		parcel.writeString(idLaporan);
		parcel.writeString(fotoLaporan);
		parcel.writeString(createdAt);
		parcel.writeString(latitudeLaporan);
		parcel.writeString(stausLaporan);
		parcel.writeString(longitudeLaporan);
		parcel.writeString(nikLaporan);
		parcel.writeString(masyarakatId);
		parcel.writeString(kelurahanLaporan);
		parcel.writeString(keteranganLaporan);
		parcel.writeString(updateAt);
		parcel.writeString(alamatLaporan);
		parcel.writeString(areaLaporan);
	}
}